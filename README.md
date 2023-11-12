
<div align="center">

<h3>CMC MakeUS 11TH 떡잎마을방범대 Project <br>
  멜리 MELLY - 장소 기반 추억 기록장 <a href=https://apps.apple.com/kr/app/%EB%A9%9C%EB%A6%AC-melly-%EC%9E%A5%EC%86%8C-%EA%B8%B0%EB%B0%98-%EC%B6%94%EC%96%B5-%EA%B8%B0%EB%A1%9D%EC%9E%A5/id6444202109>다운로드 (현재 서버 리팩토링 중입니다)</a></h3>
</div>
<br>
<br>
<div text-align: center>
<img src="https://user-images.githubusercontent.com/82302520/201556888-178a06ed-6839-43da-8194-5f48739d059d.png"  width="200" height="400">
<img src="https://user-images.githubusercontent.com/82302520/201556895-0a567bee-c4c4-466f-8615-7a804545b848.png"  width="200" height="400">
<img src="https://user-images.githubusercontent.com/82302520/201556897-a05261d3-cd7e-436b-958e-eb8ecf38f3de.png"  width="200" height="400">
<img src="https://user-images.githubusercontent.com/82302520/201556901-cd4c2cc2-0bed-4aab-8e4e-6fffe591f1d1.png"  width="200" height="400">
  </div>

<br>
<br>

## 🏗️ 서비스 구조도
![Source (6)](https://github.com/CMC11th-Melly/Melly_Server/assets/82302520/5381eaa9-f9af-48d8-aa45-0b943447fd0d)


<br>

## 📚 멀티 모듈 아키텍처
프로젝트를 구성하는 각각의 기능들을 멀티 모듈 기반으로 분리함으로써 같은 기능에 포함되는 **라이브러리 의존성간의 응집성을 높이고** 모듈별로 **독립적인 확장**을 해나갈 수 있습니다. 
```
├── client:client-auth            # OAuth 리소스 서버와 통신하는 Client 모듈 (현재 OpenFeign 의존성 사용)
├── core:core-api                 # 모바일 클라이언트와 통신하는 API 모듈    
├── storage:db-core               # MySQL 기반의 저장소 모듈
├── storage:db-redis              # Redis 기반의 인메모리 저장소 모듈
├── infra:file                    # 파일 저장소 모듈 (현재 S3 의존성 사용)     
├── infra:mail                    # 메일 서비스 모듈 (현재 Java Mail 의존성 사용)
├── infra:notification            # 알림 서비스 모듈 (현재 FCM 의존성 사용) 
└── support:logging               # 로깅 모듈          
```
<br>

## 🏛️ Database Schema
![Untitled](https://github.com/CMC11th-Melly/Melly_Server/assets/82302520/959d78e6-596b-4af7-8c12-48d6ce10e776)

## ⚙️ 주요 기능
- [기능별 유즈케이스](https://github.com/CMC11th-Melly/Melly_Server/wiki/Use-Case)
<br>

## 📋 Technical Issue

- 대용량 트래픽 상황을 가정한 로그인 기능 성능 개선
- Cursor 기반 페이징을 통한 조회 성능 최적화
- Spring Cache 적용으로 읽기 작업 성능 향상
- Token 저장용 Redis와 캐싱용 Redis 분리를 통한 가용성 확보
- Redis 분산 캐시에 Circuit Breaker 적용을 통해 Redis 서버 다운 시 DB 직접 조회를 통한 가용성 확보
- Redis LRU Eviction 정책과 서버 인스턴스 스펙에 적합한 max-memory 옵션을 적용하여 효율적인 캐시 설정
- 부하 분산을 위한 MySQL Replication 구성 및 쿼리 요청 분기
- 분산락을 사용한 동시성 제어
- AOP를 적용하여 부가 로직 제거
- 멀티 모듈 구성를 통한 의존성 격리
- 객체지향원칙 SOLID를 고려한 코드 설계
- Spring Context Caching을 통한 통합 테스트 시간 최적화
- Test Fixture를 사용한 테스트 코드 가독성 향상

