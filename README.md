<p align="center"><img src="https://user-images.githubusercontent.com/82302520/201555435-61b2b766-3b0b-4aa3-81c2-a185dccd5e2b.png"  width="100" height="100"></p>
<br>
<div align="center">
<h1>Melly Server</h1>

  <br>
<h3>CMC MakeUS 11TH 떡잎마을방범대 Project <br>
  멜리 MELLY - 장소 기반 추억 기록장 <a href=https://apps.apple.com/kr/app/%EB%A9%9C%EB%A6%AC-melly-%EC%9E%A5%EC%86%8C-%EA%B8%B0%EB%B0%98-%EC%B6%94%EC%96%B5-%EA%B8%B0%EB%A1%9D%EC%9E%A5/id6444202109>다운로드</a></h3>
</div>
<br>
<br>
<div text-align: center>
<img src="https://user-images.githubusercontent.com/82302520/201556888-178a06ed-6839-43da-8194-5f48739d059d.png"  width="200" height="400">
<img src="https://user-images.githubusercontent.com/82302520/201556895-0a567bee-c4c4-466f-8615-7a804545b848.png"  width="200" height="400">
<img src="https://user-images.githubusercontent.com/82302520/201556897-a05261d3-cd7e-436b-958e-eb8ecf38f3de.png"  width="200" height="400">
<img src="https://user-images.githubusercontent.com/82302520/201556901-cd4c2cc2-0bed-4aab-8e4e-6fffe591f1d1.png"  width="200" height="400">
<img src="https://user-images.githubusercontent.com/82302520/201556904-011e7f3f-2482-48b3-bd43-21220bc6d4bb.png" width="200" height="400">
<img src="https://user-images.githubusercontent.com/82302520/201556908-fec9b1ae-606a-4641-b24e-e02b912c7611.png"  width="200" height="400">
<img src="https://user-images.githubusercontent.com/82302520/201556911-7ba9767c-57c9-4180-935d-17dd6f22e391.png" width="200" height="400">
  </div>

<br>
<br>

## 📕 Development Environment
- <b>Language</b> :  Java 11
- <b>Framework</b> :  SpringBoot, Spring Security 
- <b>Database</b> : MySQL 8.0 , Spring Data JPA, QueryDSL
- <b>HTTP Client</b> : Web Client, okhttp3
- <b>DevOps</b> : EC2, RDS, S3, Elasticache
- <b>CI/CD</b> : Github actions, Code Deploy (Beanstalk으로 이전 예정)
- <b>Docs</b> : Swagger (SpringDocs)  

<br>

## 🗓 Development
2022.09.12 ~ 2021.11.12

<br>

## Folder Structure
```
├── MellyServerApplication.java
├── auth                                 # 인증 관련 도메인
├── block                                # 메모리 및 댓글 차단 도메인
├── comment                              # 메모리 댓글 도메인
├── common                               # 공통 응답 형식 및 유틸리티 모음
├── config                               # 인증, 문서화 등 설정 모음
├── group                                # 유저 그룹 도메인
├── healthcheck                          # 헬스 체크
├── memory                               # 메모리 도메인
├── memoryScrap                          # 메모리 스크랩 도메인
├── notification                         # 푸시 알림 도메인
├── place                                # 장소 도메인
├── placeScrap                           # 장소 스크랩 도메인
├── recommend                            # 추천 장소 도메인
├── report                               # 메모리 및 댓글 신고 도메인
├── trend                                # 핫한 장소 도메인
└── user                                 # 유저 도메인

```

<br>

## 🏛 System Structure

<br>

<p align="center">
<img src="https://user-images.githubusercontent.com/82302520/201564545-02f665c9-499a-4075-a9b3-a77e1bbe6ef3.png" width="800" height="500">
  </p>
