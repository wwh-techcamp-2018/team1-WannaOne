INSERT INTO user(id, email, password, name) VALUES (1, 'doy@woowahan.com', '$2a$10$b/fsUrWITZQ1CpB.VhaVvOmUo8fqbKoZPy4f/RwarSaBfN0.0Jk7e', 'doy');
INSERT INTO user(id, email, password, name) VALUES (2, 'dain@woowahan.com', '$2a$10$b/fsUrWITZQ1CpB.VhaVvOmUo8fqbKoZPy4f/RwarSaBfN0.0Jk7e', 'dain');
INSERT INTO note_book(id, title, owner_id) values (1, 'tech', 1);
INSERT INTO note_book(id, title, owner_id) values (2, '단위 테스트, TDD', 1);
INSERT INTO note_book(id, title, owner_id) values (3, '자바 웹 프로그래밍', 1);
INSERT INTO note_book(id, title, owner_id) values (4, 'ATDD 기반 웹 프로그래밍', 1);
INSERT INTO note_book(id, title, owner_id) values (5, 'HTML,CSS, JS 기초', 2);
INSERT INTO note_book(id, title, owner_id) values (6, '배민찬 서비스 개발', 2);
INSERT INTO note_book(id, title, owner_id) values (7, '우아한 프로젝트', 2);
INSERT INTO note(id, title, text, register_datetime, update_datetime, note_book_id, deleted, writer_id) values (1, '01.Swagger 적용하기', '## Swagger 적용하기\n\n이거 적용하는데 13초 정도 걸린거 같음.\n\n아래 의존성 추가.\n\n```yaml\ncompile group: ''io.springfox'', name: ''springfox-swagger2'', version: ''2.5.0''\ncompile group: ''io.springfox'', name: ''springfox-swagger-ui'', version: ''2.5.0''\n```\n\n아래의 설정파일 추가.\n\n```java\npackage codesquad.config;\n\nimport org.springframework.context.annotation.Bean;\nimport org.springframework.context.annotation.Configuration;\nimport springfox.documentation.builders.PathSelectors;\nimport springfox.documentation.builders.RequestHandlerSelectors;\nimport springfox.documentation.spi.DocumentationType;\nimport springfox.documentation.spring.web.plugins.Docket;\nimport springfox.documentation.swagger2.annotations.EnableSwagger2;\n\n@Configuration\n@EnableSwagger2\npublic class SwaggerConfig {\n\n    @Bean\n    public Docket api() {\n        return new Docket(DocumentationType.SWAGGER_2)\n                .select()\n                .apis(RequestHandlerSelectors.any())// 모든 RequestMapping 추출\n                .paths(PathSelectors.ant("/api/**")) // 그중에 /api/** 인 URL 필터\n                .build();\n    }\n}\n```\n\n이제 실행 후에 localhost:8080/swagger-ui.html 로 접속해보자. 끝\n','2018-08-14 13:00:00', '2018-08-14 13:54:00', 1, false, 1);
INSERT INTO note(id, title, text, register_datetime, update_datetime, note_book_id, deleted, writer_id) values (2, '02.Java 8 Stream API', '## Java 8 Stream API\n\n### 스트림이란?\n\n* 데이터 처리 연산을 지원하도록 소스에서 추출된 연속된 요소이다.\n* 다양한 데이터 소스를 표준화된 방법으로 다루기 위한 라이브러리이다.\n* 데이터 소스를 추상화하고, 데이터를 다루는데 자주 사용되는 메서드들을 정의해 놓았다.\n* 데이터소스를 추상화하였다는 것은, 데이터 소스가 무엇이든 같은 방식으로 다룰 수 있게 되었다는 것과 코드의 재사용성이 높아진다는 것을 의미한다.\n\n### 스트림의 특징\n\n1. 스트림은 데이터 소스를 변경하지 않는다.\n    * 스트림은 데이터 소스로 부터 데이터를 읽기만할 뿐, 데이터 소스를 변경하지 않는다.\n    필요하다면, 정렬된 결과를 컬렉션이나 배열에 담아서 반환할 수 있다.\n2. 스트림은 일회용이다.\n    * 스트림 한번 사용하면 닫혀서 다시 사용할 수 없다. 필요하다면 스트림을 다시 생성해야한다.\n3. 스트림은 작업 내부를 반복적으로 처리한다.\n    * 스트림을 이용한 작업이 간결할 수 있는 비결중의 하나가 바로 ''내부 반복''이다.\n    내부 반복이라는 것은 반복문을 메서드의 내부에 숨길 수 있다는 것을 의미한다.\n4. 지연된 연산\n    * 스트림 연산에서는 최종 연산이 수행되기 전까지는 중간 연산이 수행되지 않는다.\n    스트림에 대해 `sort()`나 `distinct()`같은 중간 연산을 호출해도 즉각적으로 수행되지 않는다는 것이다.\n    중간연산을 호출하는 것은 단지 어떤 작업이 수행되어야하는지를 지정해주는 것일 뿐이다.\n    최종연산이 수행되어서야 스트림의 요소들이 중간연산을 거치고 최종연산에 소모된다.\n5. 기본형 스트림\n    * 오토박싱, 언박싱으로 인한 비효율을 줄이기 위해 데이터 소스의 요소를 기본형으로 다루는 `IntStream`, `LongStream`, `DoubleStream` 이 제공된다.\n    일반적으로 `Stream< Integer>` 대신 `IntStream`을 사용하는 것이 더 효율적이고, `IntStream`에는 `int`타입으로 작업하는데 유용한 메서드들이 포함되어있다.\n6. 병렬 스트림\n    * 스트림은 내부적으로 fork&join framework를 이용해서 연산을 자동적으로 병렬로 수행한다.\n    `parallel()` 메서드를 호출하면 병렬로 연산이 수행되고, `sequential()` 메서드를 호출하면 병렬로 처리되지 않게 된다.\n    모든 스트림은 기본적으로 병렬 스트림이 아니기 때문에 `sequential()` 메서드는 `parallel()`를 취소할 때만 사용한다.\n\n### 스트림의 사용법\n\n`orders.스트림 생성().중간 연산().최종 연산()`\n\n1. 스트림을 생성한다.\n2. 초기 스트림을 다른 스트림으로 변환하는 중간 연산(intermediate operation)들을 하나 이상의 단계로 지정한다.\n3. 결과를 산출하기 위해 최종 연산(terminal operation)을 적용한다.\n이 연산은 앞선 지연 연산(lazy operation)들의 실행을 강제한다.\n이후로는 해당 스트림을 더는 사용할 수 없다.\n\n### 스트림의 주요 연산\n\n* 중간 연산\n\n| 중간 연산 | 설명 |\n| :---- | :--- |\n| `Stream < T > distinct()` | 중복 제거 |\n| `Stream < T > filter(Predicate < T > predicate)` | 조건에 안 맞는 요소 제외 |\n| `Stream < T > limit(long maxSize)` | 스트림의 일부 잘라내기 |\n| `Stream < T > skip(ling n)` | 스트림의 일부 건너뛰기 |\n| `Stream < T > peek(Consumer< T > action)` | 스트림의 요소에 작업수행 |\n| `Stream < T > sorted()` | 스트림의 요소 정렬 |\n\n* 최종 연산\n\n| 최종 연산 | 설명 |\n| :---- | :--- |\n| `void forEach(Consumer <? super T> action)` | 각 요소에 지정된 작업 수행 |\n| `long count()` | 스트림의 요소 개수 |\n| `Optional < T > max (Comparator <? super T> comparator)` | 스트림의 최댓값 |\n| `Optional < T > min (Comparator <? super T> comparator)` | 스트림의 최솟값 |\n| `Optional < T > findAny()` | 아무거나 하나 |\n| `Optional < T > findFirst()` | 스트림의 첫번째 요소 |\n| `Optional < T > reduce (BinaryOperator < T > accumulator)` | 스트림의 요소를 하나씩 줄여가면서 계산하고 최종결과를 반환 |\n| `boolean allMatch(Pradicate < T > p)` | 모두 만족하는지? |\n| `boolean anyMatch(Pradicate < T > p)` | 하나라도 만족하는지? |\n| `boolean noneMatch(Pradicate < T > p)` | 모두 만족하지 않는지? |\n| `Object[] toArray()` | 스트림의 모든 요소를 배열로 반환 |','2018-08-14 13:00:00', '2018-08-14 13:55:00', 1, false, 1);
INSERT INTO note(id, title, text, register_datetime, update_datetime, note_book_id, deleted, writer_id) values (3, '03.JPA Auditing을 이용해 생성일자 자동 생성', '## JPA Auditing 활용\n\naudit는 감시하다..이런 뜻이라고 하는데 여튼\n\n실무에서 테이블을 생성할 때 항상 그 데이터가 생성된 시점, 어떤 회원이 생성했는지에 대해서는 항상 기록을 남겼었음\n\n생성된 시점, 어떤 회원이 생성했는지, 업데이트된 시점과 업데이트된 회원에 대해서 기록을 남기는데 모든 테이블에 거의 다 적용되므로 JPA에서 Entity의 변수로 `createBy, createdDate, lastModifiedBy, lastModifiedDate` 를 작성한다면 중복이 많이 생김\n\n\n아래와 같은 기능을 활용하면 중복을 제거하면서 테이블에 대한 기록을 남길 수 있다.\n\n`JPA Auditing` 은 간단하게 말하면 생성일자, 생성자, 업데이트 일자, 업데이트 한 사람에 대한 기록을 자동으로 남겨주는 것.\n\n```java\npackage com.tram.springbootangularboard.domain;\n\nimport lombok.Getter;\nimport org.springframework.data.annotation.CreatedBy;\nimport org.springframework.data.annotation.CreatedDate;\nimport org.springframework.data.annotation.LastModifiedBy;\nimport org.springframework.data.annotation.LastModifiedDate;\nimport org.springframework.data.jpa.domain.support.AuditingEntityListener;\n\nimport javax.persistence.Column;\nimport javax.persistence.EntityListeners;\nimport javax.persistence.ManyToOne;\nimport javax.persistence.MappedSuperclass;\nimport java.time.LocalDateTime;\n\n@MappedSuperclass\n@EntityListeners(value = { AuditingEntityListener.class })\n@Getter\npublic abstract class AuditorEntity {\n    @Column(nullable = false, updatable = false)\n    @CreatedDate\n    private LocalDateTime createdDate;\n    @LastModifiedDate\n    private LocalDateTime lastModifiedDate;\n    @ManyToOne\n    @CreatedBy\n    private Account createdBy;\n    @ManyToOne @LastModifiedBy\n    private Account lastModifiedBy;\n}\n\n```\n\n이제 위와같은 BaseEntity를 상속해서 아래처럼 생성해주면 위의 데이터들을 `Repository`  에서 save할때 자동으로 넣어주게 된다.\n\n```java\npublic class Account extends AuditorEntity {\n\n    @Id\n    @GeneratedValue(strategy = GenerationType.AUTO)\n    private Long id;\n    //...\n}\n```\n\n위처럼 상속해서 사용하면 되고 `Jpa Auditing` 에 대한 `Configuration`만 추가적으로 더 넣어주면 된다.\n```java\n@EnableJpaAuditing(auditorAwareRef = "securityAuditorAware")\n@Configuration\npublic class JpaAuditingConfig {\n\n}\n```\n\n위에서 auditorAwareRef는 누구에 의해서 작성, 수정되었는지를 가져오기 위해서 현재 로그인 된 사용자의 정보를 내려주기 위해서 추가한 부분이다.\n\n```java\n@Component\npublic class SecurityAuditorAware implements AuditorAware<Account> {\n    @Autowired\n    private AccountService accountService;\n    @Override\n    public Optional<Account> getCurrentAuditor() {\n        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();\n        if(authentication instanceof AnonymousAuthenticationToken) {\n            return Optional.empty();\n        }\n        PostAuthorizationToken postAuthorizationToken = (PostAuthorizationToken)authentication;\n        return Optional.ofNullable(accountService.findByEmail(postAuthorizationToken.getAccountContext().getAccount().getEmail()));\n    }\n}\n```\n\n위와 같은 구조인데 스프링 시큐리티가 적용되어 있으므로 코드에 대해서는 신경쓰지 말고 그냥 로그인 유저의 정보를 받아오기 위한거라고 생각해주면 된다. 우리가 구현한 서비스에 맞춘다면 `@LoginUser`나 `HttpSession.getAttribute("user")` 이런 걸 반환해주면 된다. 그러면 자동으로 `createdBy`  에 현재 로그인 된 사용자 정보가 들어가게 된다.(이건 안된대 ThreadLocal에 저장되도록 변경한 다음에 TreadLocal을 이용해 가져와야 한다고 함.)\n\n내가 그냥 메모용으로 쓴 거니까 내용은 아래를 참고하는게 좋을 듯\n\n\n참고\n\n### @MappedSuperclass\n\n부모 클래스와 자식 클래스 모두 데이타베이스 테이블과 매핑을 하는데, 상속받는 자식클래스에게 매핑 정보만 제공하고 싶을때 이 어노테이션을 사용한다.\n\n[MappedSuperclass](http://feco.tistory.com/13)\n\n\n[Jpa Auditing](http://blusky10.tistory.com/316)\n', '2018-08-14 13:00:00', '2018-08-14 13:56:00', 1, false, 1);
INSERT INTO note(id, title, text, register_datetime, update_datetime, note_book_id, deleted, writer_id) values (4, 'TDD...', '### TDD 최고!!!', '2018-08-14 13:00:00', '2018-08-14 13:57:00', 2, false, 1);
INSERT INTO comment(id, content, register_datetime, update_datetime, note_id, writer_id) values(1, '첫 댓글', '2018-08-14 13:00:00', '2018-08-14 13:54:00', 1, 2);
INSERT INTO comment(id, content, register_datetime, update_datetime, note_id, writer_id) values(2, '아이디어가 떨어지네요...', '2018-08-14 13:00:00', '2018-08-14 13:54:00', 1, 1);
INSERT INTO comment(id, content, register_datetime, update_datetime, note_id, writer_id) values(3, '우아노트 화이팅!!!', '2018-08-14 13:00:00', '2018-08-14 13:54:00', 1, 2);
INSERT INTO comment(id, content, register_datetime, update_datetime, note_id, writer_id) values(4, '두번째 글의 첫 댓글', '2018-08-14 13:00:00', '2018-08-14 13:54:00', 2, 1);

INSERT INTO shared_note_book(note_book_id, user_id) values(5, 1);
INSERT INTO shared_note_book(note_book_id, user_id) values(6, 1);

INSERT INTO invitation(id, register_datetime, update_datetime, host_id, guest_id, note_book_id, status) values(1, now(), now(), 1, 2, 1, 'PENDING');
