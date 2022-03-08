# category
category api server

# spec
java17, SpringBoot 2.5.6, H2 Database, Gradle, Kotlin, flywaydb, Spring Data JPA, Query Dsl, Junit5, rest-assured, Kotest

# 실행방법

[이 곳](https://www.oracle.com/java/technologies/downloads/#jdk17-mac) jdk17을 설치합니다.

1. `git clone https://github.com/Hyung1Jung/category.git`
2. `./gradlew build`
3. `java -jar ./build/libs/category-0.0.1-SNAPSHOT.jar`

<img width="891" alt="스크린샷 2022-03-09 오전 7 31 44" src="https://user-images.githubusercontent.com/43127088/157337203-1aedba0b-f4cd-4930-b561-148e176b4b52.png">
<img width="895" alt="스크린샷 2022-03-09 오전 7 32 00" src="https://user-images.githubusercontent.com/43127088/157337253-e5c35152-f4d8-495b-91c3-bf84864f78b8.png">


또는 

1. [category-0.0.1-SNAPSHOT.jar](https://github.com/Hyung1Jung/category/blob/develop/libs/category-0.0.1-SNAPSHOT.jar) 설치 후에, 
2. 설치한 경로에서 `java -jar category-0.0.1-SNAPSHOT.jar`


# API Doc
`http://localhost:8081/swagger-ui.html`

# 문제 해결 전략

카테고리가의 depth가 깊지 않다면, 
1. JPA 연관관계 매핑과 특정한 최상위 카테고리 내에 속한 하위 카테고리의 순서를 별도의 필드인 order로 표현하고,
2. 같은 그룹 내에서, 새롭게 삽입될 카테고리보다 순서가 크거나 같은 경우 order을 +1을 하여 순서를 재조정하며 여러 기능 구현 및 정렬을 해도 될테지만,

카테고리의 depth가 깊어지는 경우 order을 탐색하는데 많은 시간이 소요될 수도 있고, 
그에 따라 작성해야 하는 쿼리나 비즈니스 로직이 매우 복잡해 질 수 있겠다는 생각이 들어 **계층적인 데이터 구조** 로 설계하였습니다.

### 따라서, 카테고리의 depth가 정말 깊어진다는 가정하에 **중첩 세트 모델(The Nested Set Model)** 로 설계하였습니다.

LEFT 필드와 RIGHT 필드로 레코드가 포함하는 범위를 결정하고, 부모는 1부터 N 까지를 포함하고 자녀들은 각자의 범위를 부모 범위 내에서 결정하게 하였습니다. 

중간에 카테고라를 삽입 혹은 삭제할 때마다 leftNode, rightNode의 값을 적절히 수정해주었습니다.
- 루트 카테고리의 경우 leftNode 및 rightNode는 1과 2로 고정됩니다.
- leftNode 및 rightNode는 동일한 카테고리 내에서의 순서 및 계층을 표현하는 값일뿐, 다른 루트 카테고리와는 독립적입니다.
- rightNode - leftNode == 1 이면 하위 카테고리가 존재하지 않고, 1이 아니면 하위 카테고리 존재합니다.
- 어떤 특정한 카테고리에 하위 카테고리를 삽입하면, 해당 하위 카테고리의 leftNode는 상위 카테고리의 rightNode가 되고 rightNode는 상위 카테고리의 rightNode + 1이 되게 하였습니다.
- 따라서 부모 카테고리 하나만 조회해도 가장 마지막에 삽입된 카테고리의 순서 정보를 알 수 있기 때문에 조회하는데 훨씬 효율적입니다.

```java
카테고리1
카테고리2
  - 카테고리2-1      
카테고리3
카테고리4
  - 카테고리4-1
카테고리5
```

- 카테고리 2와 4에 하위 카테고리가 만들어졌을 경우, 카테고리 1, 2, 3에는 변화가 없지만, 카테고리 2와 4의 leftNode, rightNode는 1과 4로 수정이 됩니다.
- 그리고 그 하위 카테고리인 2-1, 4-1은 2와 3이 됩니다.

```java
카테고리1
카테고리2
  - 카테고리2-1      
카테고리3
카테고리4
  - 카테고리4-1
        카테고리4-2 
카테고리5
```

또한, 카테고리 4-1에 하위 카테고리가 추가되었을 떄는 후위에 관된 카테고리의 leftNode, rightNode의 필드 값도 함께 수정됩니다.

이런식으로, 삽입시 leftNode, rightNode의 필드 값을 수정하면서 계층적인 중첩 모델 세트로 만들었습니다.
따라서, 카테고리의 depth가 깊어져도 leftNode, rightNode, parentCategory, rootCategory 등을 이용해 정렬하기에 더욱 빠르고 효율적인 조회가 가능합니다.

하지만, 삽입이나 수정 시에 leftNode, rightNode 또한 빈번하게 수정해야하는 문제가 있긴해서, 이 부분은 어떻게 더 효율적이고 간단하게 구현할 수 있을까 고민해보려 합니다.

기타

- 조회 시, fetch join을 이용하여 불필요하게 추가로 호출되는 쿼리들을 하나의 쿼리로 작성
- 조회 시, 캐싱 전략을 이용하여 성능 개선
- Unit test 및 Integration test 작성
