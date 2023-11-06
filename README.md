<p align="center"><img src="https://user-images.githubusercontent.com/82302520/201555435-61b2b766-3b0b-4aa3-81c2-a185dccd5e2b.png"  width="100" height="100"></p>
<br>
<div align="center">
<h1>Melly Server</h1>

  <br>
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

## 서비스 구조도
![Source (6)](https://github.com/CMC11th-Melly/Melly_Server/assets/82302520/5381eaa9-f9af-48d8-aa45-0b943447fd0d)


<br>

## 📚 Folder Structure

```
├── melly-api                     # 모바일 어플리케이션과 통신하는 REST API
├── melly-batch                   # 내부 배치 서비스        
├── melly-core                    # 서비스 로직, 도메인, 외부 모듈          
└── melly-common                  # Utils           
```

<br>

## 🏛 Domain Architecture

<img src="https://user-images.githubusercontent.com/82302520/234835879-48072114-75ba-476c-9914-a731967ca4bf.png" width="800" height="700">

- DDD 기반의 아키텍쳐로 애그리거트를 분리하는 리팩토링을 진행했습니다.
- 애그리거트 간의 필수적인 연관관계 참조가 필요없는 경우 ID 참조로 전환했습니다.

<br>


## 🏛 DTO Mapping Strategy

<img src="https://github.com/CMC11th-Melly/Melly_Server/assets/82302520/fcd5ce2f-276d-4b7a-8699-403a0610c6a9" width="700" height="500">


<br>







