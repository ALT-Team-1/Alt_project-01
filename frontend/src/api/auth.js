const BackendPort = "http://localhost:8080/api";
//백엔드 주소? 포트주소 변수로 지정

//회원가입 함수
export async function signup(email, password, nickname) {
  const response = await fetch(`${BackendPort}/auth/signup`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      email,
      password,
      nickname,
    }),
  });
  const result = await response.json();

  if (!response.ok) {
    throw new Error(result.error?.message || "회원가입에 실패했습니다.");
  }

  return result;
}

//로그인 함수
export async function login(email, password) {
  const response = await fetch(`${BackendPort}/auth/login`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      email,
      password,
    }),
  });
  const result = await response.json();

  if (!response.ok) {
    throw new Error(result.error?.message || "로그인에 실패했습니다.");
  }

  return result;
}