# Palkia

풀씨 백엔드 API 개편 프로젝트

## Getting Started

### Requirements

- Java 11 이상

### Environment Variables

**Terminal에서 실행할 경우**

```shell
export PROJECT_NAME_HERE_SECRET_KEY=example_secret_key
export EXPIRE_LENGTH_IN_MILLISECONDS=3600000
```

혹은 [direnv](https://direnv.net/) 를 사용하여 환경변수 설정을 자동화 할 수 있습니다.

### Local Development

로컬 개발은 Docker Compose로 PostgreSQL, MinIO, Spring Boot API를 함께 띄웁니다.
호스트에 Java 11이 없다면 이 방식을 사용하세요.

```shell
docker compose -f docker-compose.local.yml up -d postgres minio minio-init api
```

API가 처음 기동되면 Hibernate가 빈 DB에 테이블을 생성합니다. 그 다음 개발용 seed를 넣습니다.
이 seed는 로컬 DB의 주요 앱 테이블을 초기화하므로 운영 DB에 실행하면 안 됩니다.

```shell
docker compose -f docker-compose.local.yml exec -T postgres \
  psql -U poolc -d poolc < scripts/local-dev/seed.sql
```

개발용 계정은 모두 같은 비밀번호를 사용합니다.

```text
admin / poolc1234
president / poolc1234
member1 / poolc1234
member2 / poolc1234
member3 / poolc1234
pending / poolc1234
```

주요 로컬 주소는 다음과 같습니다.

```text
Backend API: http://localhost:8080
PostgreSQL: localhost:5432
MinIO API: http://localhost:9000
MinIO Console: http://localhost:9001
MinIO bucket: poolc-dev
MinIO login: poolc / poolc_dev_password
```

컨테이너를 중지하려면 다음 명령을 사용합니다.

```shell
docker compose -f docker-compose.local.yml down
```

**IntelliJ에서 실행할 경우**

1. Run | Edit Configurations (`⌃⌥R` + `0`)
2. Templates
3. **Gradle** 에서

- Gradle project: 프로젝트 root(/path/to/PROJECT_NAME_HERE)
- tasks: *:test*
- Environment variables: 위 환경변수 추가

4. **Spring Boot** 도 Environment variables을 설정합니다.

### Build

```shell
./gradlew build
```

### Usage

```shell
./gradlew bootRun
```

**주의:** *build & run* 및 *test* (`⌘,` > `Build, Execution, Deployment` > `Build Tools` > `Gradle`)를 intelliJ로 설정할 경우,
java
compiler(`⌘,` > `Build, Execution, Deployment` > `Compiler` > `Java Compiler` > `Additional command line parameters`)
에 `-parameters` 파라미터를 추가해야 합니다.

### Tests

```shell
./gradlew test
```

### Docs

```shell
./gradlew asciidoctor
```

`/build/asciidoc/html5/api-doc.html` 에서 api 문서를 확인할 수 있습니다.


### Deploy

TODO with docker

