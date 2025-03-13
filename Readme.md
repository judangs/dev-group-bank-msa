<p style="font-family: 'Courier New', monospace; font-size: 20px;">핀테크 서비스 플랫폼 BE</p>
<p style="font-family: 'Courier New', monospace; font-size: 15px;">Spring JPA를 이용한 MSA 환경의 플랫폼 서비스입니다.</p>


![아키텍처 개요도](./resources/images/platform_architecture.png)<p style="font-style: italic; color: gray;">
<div class="feature" style="padding: 15px; margin-bottom: 15px; background-color: #f1f8ff; border-left: 4px solid #0366d6; border-radius: 4px;">
  <strong>비고: 25.03.12: 레디스는 추후 도입 예정입니다.</strong>
</div>

<div class="container" style="background-color: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);">
  <h1 style="color: #2c3e50; margin-bottom: 20px; border-bottom: 2px solid #eaeaea; padding-bottom: 10px;">인증 / 회원 애플리케이션</h1>
  <div class="description" style="font-size: 16px; margin-bottom: 25px; color: #555;">
    회원을 관리하고 인증을 제공하는 애플리케이션입니다. <br>
  </div>
  <div class="feature" style="padding: 15px; margin-bottom: 15px; background-color: #f1f8ff; border-left: 4px solid #0366d6; border-radius: 4px;">
    <strong>기능:</strong> 회원 관리 / 인증 / 토큰 발행 및 재발행 / 인증 메일 관리
  </div>
  <div class="tech-stack" style="display: flex; flex-wrap: wrap; gap: 15px; margin-top: 25px;">
    <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=data:image/svg%2bxml;base64,PCFET0NUWVBFIHN2ZyBQVUJMSUMgIi0vL1czQy8vRFREIFNWRyAxLjEvL0VOIiAiaHR0cDovL3d3dy53My5vcmcvR3JhcGhpY3MvU1ZHLzEuMS9EVEQvc3ZnMTEuZHRkIj4KDTwhLS0gVXBsb2FkZWQgdG86IFNWRyBSZXBvLCB3d3cuc3ZncmVwby5jb20sIFRyYW5zZm9ybWVkIGJ5OiBTVkcgUmVwbyBNaXhlciBUb29scyAtLT4KPHN2ZyB3aWR0aD0iMTUwcHgiIGhlaWdodD0iMTUwcHgiIHZpZXdCb3g9IjAgMCAzMi4wMCAzMi4wMCIgdmVyc2lvbj0iMS4xIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHhtbG5zOnhsaW5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hsaW5rIiBmaWxsPSIjZmZmZmZmIiBzdHJva2U9IiNmZmZmZmYiIHN0cm9rZS13aWR0aD0iMC4yNTYiPgoNPGcgaWQ9IlNWR1JlcG9fYmdDYXJyaWVyIiBzdHJva2Utd2lkdGg9IjAiLz4KDTxnIGlkPSJTVkdSZXBvX3RyYWNlckNhcnJpZXIiIHN0cm9rZS1saW5lY2FwPSJyb3VuZCIgc3Ryb2tlLWxpbmVqb2luPSJyb3VuZCIvPgoNPGcgaWQ9IlNWR1JlcG9faWNvbkNhcnJpZXIiPiA8cGF0aCBmaWxsPSIjZmZmZmZmIiBkPSJNMTIuNTU3IDIzLjIyYzAgMC0wLjk4MiAwLjU3MSAwLjY5OSAwLjc2NSAyLjAzNyAwLjIzMiAzLjA3OSAwLjE5OSA1LjMyNC0wLjIyNiAwIDAgMC41OSAwLjM3IDEuNDE1IDAuNjkxLTUuMDMzIDIuMTU3LTExLjM5LTAuMTI1LTcuNDM3LTEuMjN6TTExLjk0MiAyMC40MDVjMCAwLTEuMTAyIDAuODE2IDAuNTgxIDAuOTkgMi4xNzYgMC4yMjQgMy44OTUgMC4yNDMgNi44NjktMC4zMyAwIDAgMC40MTEgMC40MTcgMS4wNTggMC42NDUtNi4wODUgMS43NzktMTIuODYzIDAuMTQtOC41MDgtMS4zMDV6TTE3LjEyNyAxNS42M2MxLjI0IDEuNDI4LTAuMzI2IDIuNzEzLTAuMzI2IDIuNzEzczMuMTQ5LTEuNjI1IDEuNzAzLTMuNjYxYy0xLjM1MS0xLjg5OC0yLjM4Ni0yLjg0MSAzLjIyMS02LjA5MyAwIDAtOC44MDEgMi4xOTgtNC41OTggNy4wNDJ6TTIzLjc4MyAyNS4zMDJjMCAwIDAuNzI3IDAuNTk5LTAuODAxIDEuMDYyLTIuOTA1IDAuODgtMTIuMDkxIDEuMTQ2LTE0LjY0MyAwLjAzNS0wLjkxNy0wLjM5OSAwLjgwMy0wLjk1MyAxLjM0NC0xLjA2OSAwLjU2NC0wLjEyMiAwLjg4Ny0wLjEgMC44ODctMC4xLTEuMDIwLTAuNzE5LTYuNTk0IDEuNDExLTIuODMxIDIuMDIxIDEwLjI2MiAxLjY2NCAxOC43MDYtMC43NDkgMTYuMDQ0LTEuOTV6TTEzLjAyOSAxNy40ODljMCAwLTQuNjczIDEuMTEtMS42NTUgMS41MTMgMS4yNzQgMC4xNzEgMy44MTQgMC4xMzIgNi4xODEtMC4wNjYgMS45MzQtMC4xNjMgMy44NzYtMC41MSAzLjg3Ni0wLjUxcy0wLjY4MiAwLjI5Mi0xLjE3NSAwLjYyOWMtNC43NDUgMS4yNDgtMTMuOTExIDAuNjY3LTExLjI3Mi0wLjYwOSAyLjIzMi0xLjA3OSA0LjA0Ni0wLjk1NiA0LjA0Ni0wLjk1NnpNMjEuNDEyIDIyLjE3NGM0LjgyNC0yLjUwNiAyLjU5My00LjkxNSAxLjAzNy00LjU5MS0wLjM4MiAwLjA3OS0wLjU1MiAwLjE0OC0wLjU1MiAwLjE0OHMwLjE0Mi0wLjIyMiAwLjQxMi0wLjMxOGMzLjA3OS0xLjA4MyA1LjQ0OCAzLjE5My0wLjk5NCA0Ljg4Ny0wIDAgMC4wNzUtMC4wNjcgMC4wOTctMC4xMjZ6TTE4LjUwMyAzLjMzN2MwIDAgMi42NzEgMi42NzItMi41MzQgNi43ODEtNC4xNzQgMy4yOTYtMC45NTIgNS4xNzYtMC4wMDIgNy4zMjMtMi40MzYtMi4xOTgtNC4yMjQtNC4xMzMtMy4wMjUtNS45MzQgMS43NjEtMi42NDQgNi42MzgtMy45MjUgNS41Ni04LjE3ek0xMy41MDMgMjguOTY2YzQuNjMgMC4yOTYgMTEuNzQtMC4xNjQgMTEuOTA4LTIuMzU1IDAgMC0wLjMyNCAwLjgzMS0zLjgyNiAxLjQ5LTMuOTUyIDAuNzQ0LTguODI2IDAuNjU3LTExLjcxNiAwLjE4IDAgMCAwLjU5MiAwLjQ5IDMuNjM1IDAuNjg1eiIvPiA8L2c+Cg08L3N2Zz4=" style="border-radius:10px"/>
    <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white" style="border-radius:10px">
    <img src="https://img.shields.io/badge/Apache Kafka-%3333333.svg?style=for-the-badge&logo=Apache Kafka&logoColor=white" style="border-radius:10px">
    <img src="https://img.shields.io/badge/JUnit5/AssertJ-25A162?style=for-the-badge&logo=JUnit5&logoColor=white" style="border-radius:10px">
    <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON Web Tokens&logoColor=white" style="border-radius:10px">
    <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=Swagger&logoColor=white" style="border-radius:10px">
  </div>

  <h1 style="color: #2c3e50; margin-bottom: 20px; border-bottom: 2px solid #eaeaea; padding-bottom: 10px;">페이 애플리케이션</h1>
  <div class="description" style="font-size: 16px; margin-bottom: 25px; color: #555;">
    플랫폼이 제공하는 서비스를 이용하거나 구매하기 위한 온라인 재화를 충전하거나 그룹 단위로 결제 상품을 관리할 수 있습니다. <br>
  </div>
  <div class="feature" style="padding: 15px; margin-bottom: 15px; background-color: #f1f8ff; border-left: 4px solid #0366d6; border-radius: 4px;">
    <strong>기능:</strong> 플랫폼이 발행한 결제 카드 / 결제(캐시 충전) 이벤트 발행 / 공유 캐시 그룹의 이벤트 발행(초대, 상품 결제 요청)
  </div>
  <div class="tech-stack" style="display: flex; flex-wrap: wrap; gap: 15px; margin-top: 25px;">
    <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=data:image/svg%2bxml;base64,PCFET0NUWVBFIHN2ZyBQVUJMSUMgIi0vL1czQy8vRFREIFNWRyAxLjEvL0VOIiAiaHR0cDovL3d3dy53My5vcmcvR3JhcGhpY3MvU1ZHLzEuMS9EVEQvc3ZnMTEuZHRkIj4KDTwhLS0gVXBsb2FkZWQgdG86IFNWRyBSZXBvLCB3d3cuc3ZncmVwby5jb20sIFRyYW5zZm9ybWVkIGJ5OiBTVkcgUmVwbyBNaXhlciBUb29scyAtLT4KPHN2ZyB3aWR0aD0iMTUwcHgiIGhlaWdodD0iMTUwcHgiIHZpZXdCb3g9IjAgMCAzMi4wMCAzMi4wMCIgdmVyc2lvbj0iMS4xIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHhtbG5zOnhsaW5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hsaW5rIiBmaWxsPSIjZmZmZmZmIiBzdHJva2U9IiNmZmZmZmYiIHN0cm9rZS13aWR0aD0iMC4yNTYiPgoNPGcgaWQ9IlNWR1JlcG9fYmdDYXJyaWVyIiBzdHJva2Utd2lkdGg9IjAiLz4KDTxnIGlkPSJTVkdSZXBvX3RyYWNlckNhcnJpZXIiIHN0cm9rZS1saW5lY2FwPSJyb3VuZCIgc3Ryb2tlLWxpbmVqb2luPSJyb3VuZCIvPgoNPGcgaWQ9IlNWR1JlcG9faWNvbkNhcnJpZXIiPiA8cGF0aCBmaWxsPSIjZmZmZmZmIiBkPSJNMTIuNTU3IDIzLjIyYzAgMC0wLjk4MiAwLjU3MSAwLjY5OSAwLjc2NSAyLjAzNyAwLjIzMiAzLjA3OSAwLjE5OSA1LjMyNC0wLjIyNiAwIDAgMC41OSAwLjM3IDEuNDE1IDAuNjkxLTUuMDMzIDIuMTU3LTExLjM5LTAuMTI1LTcuNDM3LTEuMjN6TTExLjk0MiAyMC40MDVjMCAwLTEuMTAyIDAuODE2IDAuNTgxIDAuOTkgMi4xNzYgMC4yMjQgMy44OTUgMC4yNDMgNi44NjktMC4zMyAwIDAgMC40MTEgMC40MTcgMS4wNTggMC42NDUtNi4wODUgMS43NzktMTIuODYzIDAuMTQtOC41MDgtMS4zMDV6TTE3LjEyNyAxNS42M2MxLjI0IDEuNDI4LTAuMzI2IDIuNzEzLTAuMzI2IDIuNzEzczMuMTQ5LTEuNjI1IDEuNzAzLTMuNjYxYy0xLjM1MS0xLjg5OC0yLjM4Ni0yLjg0MSAzLjIyMS02LjA5MyAwIDAtOC44MDEgMi4xOTgtNC41OTggNy4wNDJ6TTIzLjc4MyAyNS4zMDJjMCAwIDAuNzI3IDAuNTk5LTAuODAxIDEuMDYyLTIuOTA1IDAuODgtMTIuMDkxIDEuMTQ2LTE0LjY0MyAwLjAzNS0wLjkxNy0wLjM5OSAwLjgwMy0wLjk1MyAxLjM0NC0xLjA2OSAwLjU2NC0wLjEyMiAwLjg4Ny0wLjEgMC44ODctMC4xLTEuMDIwLTAuNzE5LTYuNTk0IDEuNDExLTIuODMxIDIuMDIxIDEwLjI2MiAxLjY2NCAxOC43MDYtMC43NDkgMTYuMDQ0LTEuOTV6TTEzLjAyOSAxNy40ODljMCAwLTQuNjczIDEuMTEtMS42NTUgMS41MTMgMS4yNzQgMC4xNzEgMy44MTQgMC4xMzIgNi4xODEtMC4wNjYgMS45MzQtMC4xNjMgMy44NzYtMC41MSAzLjg3Ni0wLjUxcy0wLjY4MiAwLjI5Mi0xLjE3NSAwLjYyOWMtNC43NDUgMS4yNDgtMTMuOTExIDAuNjY3LTExLjI3Mi0wLjYwOSAyLjIzMi0xLjA3OSA0LjA0Ni0wLjk1NiA0LjA0Ni0wLjk1NnpNMjEuNDEyIDIyLjE3NGM0LjgyNC0yLjUwNiAyLjU5My00LjkxNSAxLjAzNy00LjU5MS0wLjM4MiAwLjA3OS0wLjU1MiAwLjE0OC0wLjU1MiAwLjE0OHMwLjE0Mi0wLjIyMiAwLjQxMi0wLjMxOGMzLjA3OS0xLjA4MyA1LjQ0OCAzLjE5My0wLjk5NCA0Ljg4Ny0wIDAgMC4wNzUtMC4wNjcgMC4wOTctMC4xMjZ6TTE4LjUwMyAzLjMzN2MwIDAgMi42NzEgMi42NzItMi41MzQgNi43ODEtNC4xNzQgMy4yOTYtMC45NTIgNS4xNzYtMC4wMDIgNy4zMjMtMi40MzYtMi4xOTgtNC4yMjQtNC4xMzMtMy4wMjUtNS45MzQgMS43NjEtMi42NDQgNi42MzgtMy45MjUgNS41Ni04LjE3ek0xMy41MDMgMjguOTY2YzQuNjMgMC4yOTYgMTEuNzQtMC4xNjQgMTEuOTA4LTIuMzU1IDAgMC0wLjMyNCAwLjgzMS0zLjgyNiAxLjQ5LTMuOTUyIDAuNzQ0LTguODI2IDAuNjU3LTExLjcxNiAwLjE4IDAgMCAwLjU5MiAwLjQ5IDMuNjM1IDAuNjg1eiIvPiA8L2c+Cg08L3N2Zz4=" style="border-radius:10px"/>
    <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white" style="border-radius:10px">
    <img src="https://img.shields.io/badge/Apache Kafka-%3333333.svg?style=for-the-badge&logo=Apache Kafka&logoColor=white" style="border-radius:10px">
    <img src="https://img.shields.io/badge/JUnit5/AssertJ-25A162?style=for-the-badge&logo=JUnit5&logoColor=white" style="border-radius:10px">
    <img src="https://img.shields.io/badge/naver pay-000000?style=for-the-badge&logo=data:image/svg%2bxml;base64,PHN2ZyB3aWR0aD0iMTk4IiBoZWlnaHQ9IjY2IiB2aWV3Qm94PSIwIDAgMTk4IDY2IiBmaWxsPSJub25lIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciPgo8cGF0aCBkPSJNNjYgMzNDNjYgMzkuNTI2OCA2NC4wNjQ2IDQ1LjkwNyA2MC40Mzg1IDUxLjMzMzhDNTYuODEyNCA1Ni43NjA2IDUxLjY1ODUgNjAuOTkwMyA0NS42Mjg2IDYzLjQ4OEMzOS41OTg2IDY1Ljk4NTcgMzIuOTYzNCA2Ni42MzkyIDI2LjU2MiA2NS4zNjU5QzIwLjE2MDcgNjQuMDkyNiAxNC4yODA2IDYwLjk0OTYgOS42NjU1IDU2LjMzNDVDNS4wNTAzNiA1MS43MTk0IDEuOTA3NDIgNDUuODM5NCAwLjYzNDEwNCAzOS40MzhDLTAuNjM5MjA4IDMzLjAzNjYgMC4wMTQyODg5IDI2LjQwMTQgMi41MTE5OCAyMC4zNzE0QzUuMDA5NjcgMTQuMzQxNSA5LjIzOTM4IDkuMTg3NTggMTQuNjY2MiA1LjU2MTVDMjAuMDkzIDEuOTM1NDEgMjYuNDczMiAwIDMzIDBDNDEuNzUyMiAwIDUwLjE0NTggMy40NzY3OCA1Ni4zMzQ1IDkuNjY1NDhDNjIuNTIzMiAxNS44NTQyIDY2IDI0LjI0NzkgNjYgMzNaTTM4LjUxOTMgMTcuNDlWMzQuMDgwN0wyNi45NjkzIDE3LjQ5SDE3LjQ5VjQ4LjU1MTNIMjcuNDcyNVYzMS45MTFMMzkuMDIyNSA0OC41MUg0OC41MzQ4VjE3LjQ5SDM4LjUxOTNaIiBmaWxsPSIjMDNDNzVBIi8+CjxwYXRoIGQ9Ik0xODQuMjk2IDE1LjY1ODNMMTc1LjE3NCAzNi40NDM0TDE2NC44NTUgMTUuNjU4M0gxNTYuOTIyTDE3MS41MzUgNDQuNDk1TDE2NS41MTEgNTguMDA3NEgxNzMuMjE1TDE5MiAxNS42OTkzTDE4NC4yOTYgMTUuNjU4M1pNMTUxLjg1MSA0Ny43OTkzSDE0NC4zMTFWNDQuNzk4NEMxNDEuNTY4IDQ3LjIwMjcgMTM4LjAzMiA0OC41MDU1IDEzNC4zODYgNDguNDU1MkMxMjUuMzcgNDguNDU1MiAxMTguMzE0IDQxLjA3NTkgMTE4LjMxNCAzMS43Mjg4QzExOC4zMTQgMjIuMzgxNyAxMjUuMzYyIDE1LjAwMjQgMTM0LjM4NiAxNS4wMDI0QzEzOC4wMzEgMTQuOTQ5OSAxNDEuNTY3IDE2LjI0OTcgMTQ0LjMxMSAxOC42NTFWMTUuNjU4M0gxNTEuODUxVjQ3Ljc5OTNaTTE0NS4xOTYgMzEuNzY5OEMxNDUuMTk2IDI1LjcxMDYgMTQwLjk2NyAyMS4xMTA4IDEzNS4zNjEgMjEuMTEwOEMxMjkuNzU1IDIxLjExMDggMTI1LjUyNiAyNS43MTA2IDEyNS41MjYgMzEuNzY5OEMxMjUuNTI2IDM3LjgyOSAxMjkuNzQ3IDQyLjQyODggMTM1LjM2MSA0Mi40Mjg4QzE0MC45NzUgNDIuNDI4OCAxNDUuMTk2IDM3Ljc4OCAxNDUuMTk2IDMxLjc2OThaTTgxLjAyNDYgNTguMDA3NEg4OC44OTI2VjQ1LjA2OUM5MS41ODQ3IDQ3LjMwODUgOTQuOTg5MyA0OC41MDk3IDk4LjQ5IDQ4LjQ1NTJDMTA3LjUwNSA0OC40NTUyIDExNC41NyA0MS4wNzU5IDExNC41NyAzMS43Mjg4QzExNC41NyAyMi4zODE3IDEwNy41MTQgMTUuMDAyNCA5OC40OSAxNS4wMDI0Qzk0LjgzNSAxNC45MzY3IDkxLjI4NyAxNi4yMzc4IDg4LjU0MDIgMTguNjUxVjE1LjY1ODNIODFMODEuMDI0NiA1OC4wMDc0Wk05Ny41NTU3IDIxLjExMDhDMTAzLjE2MiAyMS4xMTA4IDEwNy4zOTEgMjUuNzEwNiAxMDcuMzkxIDMxLjc2OThDMTA3LjM5MSAzNy44MjkgMTAzLjE2MiA0Mi40Mjg4IDk3LjU1NTcgNDIuNDI4OEM5MS45NDk3IDQyLjQyODggODcuNzIwNiAzNy44MjkgODcuNzIwNiAzMS43Njk4Qzg3LjcyMDYgMjUuNzEwNiA5MS45MDA1IDIxLjExMDggOTcuNTU1NyAyMS4xMTA4WiIgZmlsbD0iIzAzQzc1QSIvPgo8L3N2Zz4K" style="border-radius:10px"/>
    <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=Swagger&logoColor=white" style="border-radius:10px">
  </div>

  <h1 style="color: #2c3e50; margin-bottom: 20px; border-bottom: 2px solid #eaeaea; padding-bottom: 10px;">게이트웨이 애플리케이션</h1>
  <div class="description" style="font-size: 16px; margin-bottom: 25px; color: #555;">
    사용자의 인증 및 인가를 담당하고 적절한 API 호출이 이루어질 수 있도록 API 애플리케이션 서비스로의 라우팅을 담당하고 있습니다.
  </div>
  <div class="feature" style="padding: 15px; margin-bottom: 15px; background-color: #f1f8ff; border-left: 4px solid #0366d6; border-radius: 4px;">
    <strong>특징:</strong> 비동기로 사용자의 요청을 처리할 수 있도록 스프링 게이트웨이를 활용한 스프링 부트 환경에서 WebFlux 기술을 이용했습니다.
  </div>
  <div class="tech-stack" style="display: flex; flex-wrap: wrap; gap: 15px; margin-top: 25px;">
    <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white" style="border-radius:10px">
    <img src="https://img.shields.io/badge/Spring Cloud-6DB33F?style=for-the-badge&logo=iCloud&logoColor=white" style="border-radius:10px">
    <img src="https://img.shields.io/badge/Spring Webflux-6DB33F?style=for-the-badge&logo=React&logoColor=white" style="border-radius:10px">
    <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON Web Tokens&logoColor=white" style="border-radius:10px">
  </div>

  <h1 style="color: #2c3e50; margin-bottom: 20px; border-bottom: 2px solid #eaeaea; padding-bottom: 10px;">플랫폼 컨슈머 애플리케이션</h1>
  <div class="description" style="font-size: 16px; margin-bottom: 25px; color: #555;">
    플랫폼 내에서 발생하는 카프카 메시지(이벤트)를 처리하는 애플리케이션입니다. 이벤트 구분 및 범위에 따라 플랫폼에서 사용하는 데이터베이스들에 접근할 수 있도록 데이터소스 및 트랜잭션을 구분하고 설정할 수 있도록 했습니다. <br>
  </div>
  <div class="feature" style="padding: 15px; margin-bottom: 15px; background-color: #f1f8ff; border-left: 4px solid #0366d6; border-radius: 4px;">
    <strong>특징:</strong> 이벤트는 크게 계정 / 결제 / 구매 / 계정 그룹 이벤트로 나누어볼 수 있고 애플리케이션이 적절하게 이벤트를 처리합니다.
  </div>
  <div class="tech-stack" style="display: flex; flex-wrap: wrap; gap: 15px; margin-top: 25px;">
    <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=data:image/svg%2bxml;base64,PCFET0NUWVBFIHN2ZyBQVUJMSUMgIi0vL1czQy8vRFREIFNWRyAxLjEvL0VOIiAiaHR0cDovL3d3dy53My5vcmcvR3JhcGhpY3MvU1ZHLzEuMS9EVEQvc3ZnMTEuZHRkIj4KDTwhLS0gVXBsb2FkZWQgdG86IFNWRyBSZXBvLCB3d3cuc3ZncmVwby5jb20sIFRyYW5zZm9ybWVkIGJ5OiBTVkcgUmVwbyBNaXhlciBUb29scyAtLT4KPHN2ZyB3aWR0aD0iMTUwcHgiIGhlaWdodD0iMTUwcHgiIHZpZXdCb3g9IjAgMCAzMi4wMCAzMi4wMCIgdmVyc2lvbj0iMS4xIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHhtbG5zOnhsaW5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hsaW5rIiBmaWxsPSIjZmZmZmZmIiBzdHJva2U9IiNmZmZmZmYiIHN0cm9rZS13aWR0aD0iMC4yNTYiPgoNPGcgaWQ9IlNWR1JlcG9fYmdDYXJyaWVyIiBzdHJva2Utd2lkdGg9IjAiLz4KDTxnIGlkPSJTVkdSZXBvX3RyYWNlckNhcnJpZXIiIHN0cm9rZS1saW5lY2FwPSJyb3VuZCIgc3Ryb2tlLWxpbmVqb2luPSJyb3VuZCIvPgoNPGcgaWQ9IlNWR1JlcG9faWNvbkNhcnJpZXIiPiA8cGF0aCBmaWxsPSIjZmZmZmZmIiBkPSJNMTIuNTU3IDIzLjIyYzAgMC0wLjk4MiAwLjU3MSAwLjY5OSAwLjc2NSAyLjAzNyAwLjIzMiAzLjA3OSAwLjE5OSA1LjMyNC0wLjIyNiAwIDAgMC41OSAwLjM3IDEuNDE1IDAuNjkxLTUuMDMzIDIuMTU3LTExLjM5LTAuMTI1LTcuNDM3LTEuMjN6TTExLjk0MiAyMC40MDVjMCAwLTEuMTAyIDAuODE2IDAuNTgxIDAuOTkgMi4xNzYgMC4yMjQgMy44OTUgMC4yNDMgNi44NjktMC4zMyAwIDAgMC40MTEgMC40MTcgMS4wNTggMC42NDUtNi4wODUgMS43NzktMTIuODYzIDAuMTQtOC41MDgtMS4zMDV6TTE3LjEyNyAxNS42M2MxLjI0IDEuNDI4LTAuMzI2IDIuNzEzLTAuMzI2IDIuNzEzczMuMTQ5LTEuNjI1IDEuNzAzLTMuNjYxYy0xLjM1MS0xLjg5OC0yLjM4Ni0yLjg0MSAzLjIyMS02LjA5MyAwIDAtOC44MDEgMi4xOTgtNC41OTggNy4wNDJ6TTIzLjc4MyAyNS4zMDJjMCAwIDAuNzI3IDAuNTk5LTAuODAxIDEuMDYyLTIuOTA1IDAuODgtMTIuMDkxIDEuMTQ2LTE0LjY0MyAwLjAzNS0wLjkxNy0wLjM5OSAwLjgwMy0wLjk1MyAxLjM0NC0xLjA2OSAwLjU2NC0wLjEyMiAwLjg4Ny0wLjEgMC44ODctMC4xLTEuMDIwLTAuNzE5LTYuNTk0IDEuNDExLTIuODMxIDIuMDIxIDEwLjI2MiAxLjY2NCAxOC43MDYtMC43NDkgMTYuMDQ0LTEuOTV6TTEzLjAyOSAxNy40ODljMCAwLTQuNjczIDEuMTEtMS42NTUgMS41MTMgMS4yNzQgMC4xNzEgMy44MTQgMC4xMzIgNi4xODEtMC4wNjYgMS45MzQtMC4xNjMgMy44NzYtMC41MSAzLjg3Ni0wLjUxcy0wLjY4MiAwLjI5Mi0xLjE3NSAwLjYyOWMtNC43NDUgMS4yNDgtMTMuOTExIDAuNjY3LTExLjI3Mi0wLjYwOSAyLjIzMi0xLjA3OSA0LjA0Ni0wLjk1NiA0LjA0Ni0wLjk1NnpNMjEuNDEyIDIyLjE3NGM0LjgyNC0yLjUwNiAyLjU5My00LjkxNSAxLjAzNy00LjU5MS0wLjM4MiAwLjA3OS0wLjU1MiAwLjE0OC0wLjU1MiAwLjE0OHMwLjE0Mi0wLjIyMiAwLjQxMi0wLjMxOGMzLjA3OS0xLjA4MyA1LjQ0OCAzLjE5My0wLjk5NCA0Ljg4Ny0wIDAgMC4wNzUtMC4wNjcgMC4wOTctMC4xMjZ6TTE4LjUwMyAzLjMzN2MwIDAgMi42NzEgMi42NzItMi41MzQgNi43ODEtNC4xNzQgMy4yOTYtMC45NTIgNS4xNzYtMC4wMDIgNy4zMjMtMi40MzYtMi4xOTgtNC4yMjQtNC4xMzMtMy4wMjUtNS45MzQgMS43NjEtMi42NDQgNi42MzgtMy45MjUgNS41Ni04LjE3ek0xMy41MDMgMjguOTY2YzQuNjMgMC4yOTYgMTEuNzQtMC4xNjQgMTEuOTA4LTIuMzU1IDAgMC0wLjMyNCAwLjgzMS0zLjgyNiAxLjQ5LTMuOTUyIDAuNzQ0LTguODI2IDAuNjU3LTExLjcxNiAwLjE4IDAgMCAwLjU5MiAwLjQ5IDMuNjM1IDAuNjg1eiIvPiA8L2c+Cg08L3N2Zz4=" style="border-radius:10px"/>
    <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white" style="border-radius:10px">
    <img src="https://img.shields.io/badge/Apache Kafka-%3333333.svg?style=for-the-badge&logo=Apache Kafka&logoColor=white" style="border-radius:10px">
    <img src="https://img.shields.io/badge/JUnit5/AssertJ-25A162?style=for-the-badge&logo=JUnit5&logoColor=white" style="border-radius:10px">
  </div>

  <h1 style="color: #2c3e50; margin-bottom: 20px; border-bottom: 2px solid #eaeaea; padding-bottom: 10px;">멀티모듈 프로젝트와 개발 환경</h1>
  <div class="description" style="font-size: 16px; margin-bottom: 25px; color: #555;">
  </div>
  <h3 style="color: black">데이터 저장소</h3>
  <div class="tech-stack" style="display: flex; flex-wrap: wrap; gap: 15px; margin-top: 25px;">
    <img src="https://img.shields.io/badge/spring data jpa-8BC0D0?style=for-the-badge&logoColor=white&logo=data:image/svg%2bxml;base64,PHN2ZyBpZD0iTGF5ZXJfMSIgZGF0YS1uYW1lPSJMYXllciAxIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAxMTYuODggMTQ1Ljk3Ij48ZGVmcz48c3R5bGU+LmNscy0xe2ZpbGw6IzZkYjMzZjt9PC9zdHlsZT48L2RlZnM+PHRpdGxlPmxvZ28tZGF0YTwvdGl0bGU+PHBhdGggY2xhc3M9ImNscy0xIiBkPSJNNTguMzMsMTAxLjc5QzI5LjU0LDEwMS43OSwxNyw5OS40MiwwLDk1LjQ2djQ0bC44LjJjMTYuNCw0LDI4LjgsNi4zMiw1Ny41Myw2LjMyczQxLjM1LTIuMzgsNTcuNzQtNi4zN2wuODEtLjJ2LTQ0Qzk5LjkzLDk5LjQsODcuMjcsMTAxLjc5LDU4LjMzLDEwMS43OVoiLz48cGF0aCBjbGFzcz0iY2xzLTEiIGQ9Ik01OC4zMywwQzI5LjYsMCwxNy4yLDIuMzYuOCw2LjMybC0uOC4yVjUzYzE3LTQsMjkuNTQtNi4zMyw1OC4zMy02LjMzczQxLjYsMi4zOSw1OC41NSw2LjM5VjYuNTdsLS44MS0uMkM5OS42OCwyLjM4LDg3LjIxLDAsNTguMzMsMFoiLz48cGF0aCBjbGFzcz0iY2xzLTEiIGQ9Ik0xMTYuODgsNTUuNThsLS44MS0uMkM5OS42OCw1MS4zOSw4Ny4yMSw0OSw1OC4zMyw0OVMxNy4yLDUxLjM3LjgsNTUuMzJsLS44LjJWOTIuOTRsLjguMTljMTYuNCw0LDI4LjgsNi4zMyw1Ny41Myw2LjMzczQxLjM1LTIuMzksNTcuNzQtNi4zOGwuODEtLjJaIi8+PC9zdmc+" style="border-radius:10px"/>
    <img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white" style="border-radius:10px">
    <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white" style="border-radius:10px">
  </div>

  <h3 style="color: black">개발 환경</h3>
  <div class="tech-stack" style="display: flex; flex-wrap: wrap; gap: 15px; margin-top: 25px;">
    <img src="https://img.shields.io/badge/intellijidea-000000.svg?style=for-the-badge&logo=intellijidea&logoColor=white" style="border-radius:10px">
    <img src="https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white" style="border-radius:10px">
    <img src="https://img.shields.io/badge/gradle-02303A.svg?style=for-the-badge&logo=gradle&logoColor=white" style="border-radius:10px">
  </div>
</div>

## 멀티 모듈 구성

![멀티 모듈 구성도](./resources/images/platform_multi_modlues.png)

|         모듈         | 설명                                                                                       |
|:------------------:|:-----------------------------------------------------------------------------------------|
|     **api 모듈**     | **외부에 공개되는 엔드 포인트를 제공하는 모듈들**                                                            |
| **application 모듈** | **애플리케이션 모듈로써 도메인 모듈의 다양한 도메인 서비스를 한 곳에 모아 실행할 수 있는 영역<br/>(ex. 컨슈머 모듈)**                |
|   **domain 모듈**    | **도메인이 필요한 오브젝트와 밸류타입, 그리고 도메인이 제공하는 기능을 수행할 수 있도록 도메인 서비스**                             |
|    **store 모듈**    | **데이터 저장소의 connection에 대한 설정과 레포지토리의 구현체**                                               |
|    **core 모듈**     | **다양한 모듈들이 공유하는 공통 모듈로써 밸류 타입, 예외, 인터페이스 등. 최대한 POJO와 가깝게 작성하려 노력을 많이 기울였던 모듈**          |
|   **실행 가능한 모듈**    | **:bank-api:techfin-user, bank-api:techfin-pay, bank-application:techfin-core-consumer** |

### 모듈 설정 및 실행
```bash

# 의존성 체크 후 실행 가능한 모듈 실행 예시
./gradlew clean
./gradlew :bank-api:techfin-user-api:build --refresh-dependencies
./gradlew :bank-api:techfin-pay-api:build --refresh-dependencies

# 실행 환경에 맞춰 데이터 소스를 로드할 수 있도록 profile 설정
profile: {"production", "develop", "test"}

# 리팩토링 이후 redis를 제거하며 제거된 기능에 대해서는 릴리즈에서 Mock을 활용한 임시 객체로 작성
```

## 참고 자료 및 일정 관리
- **페이 애플리케이션 개발 전 이벤트 스토밍**
-![기획: 페이 애플리케이션 이벤트 스토밍](./resources/images/app-pay-planning-eventstorming.png)
- **일정 참고 자료**
  - 👉🏻 [개발 일지](https://www.notion.so/Techfin-10a86943310e80059e4cc4fd8f2f5d44#15186943310e80b59f85dc5f763a333a)
  - 비고: 현재 개발 내용과 상이한 부분이 있을 수 있습니다. (+ github을 이용한 일정 관리)