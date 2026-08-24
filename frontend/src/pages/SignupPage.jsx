import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { signup } from "../api/auth.js";
import "../css/Signup.css"


export default function SignupPage() {
  const navigate = useNavigate();
  //const 사용이유:화면에 바로바로 표시되게 할려고
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [nickname, setNickname] = useState("");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);


  //비동기 처리
  const handleSubmit = async (event) => {
    //폼 제출시 새로고침 방지
    event.preventDefault();
    setMessage("");
    setError("");
    if (password.length < 8) {
      setError("비밀번호는 8자 이상이어야 합니다.");
      return;
    }

    //try 사용이유:오류나면 catch로 처리할려고
    try {
      setIsLoading(true);
      //이멜,패스워드,닉네임전달
      const result = 
          await signup(email, password, nickname);
      localStorage.setItem(
        "accessToken",
         result.data.accessToken
      );
      localStorage.setItem(
        "userId",
        result.data.userId
      );
      localStorage.setItem(
        "nickname",
        result.data.nickname
      );
      setMessage(
        `${result.data.nickname}님, 회원가입이 완료되었습니다.`
      );
      navigate("/", { replace: true });

      } catch (err) {
        setError(err.message);
      } finally {
        setIsLoading(false);
      }
    };



  return (
    <main>
      <h1 id="maintext">회원가입</h1>
      <form onSubmit={handleSubmit}>
          <div id="input_area">
            <div id="Signup_email_div">
              <div>
              <label htmlFor="email">이메일</label>
              </div>
              <input
                id="email"
                type="email"
                value={email}
                onChange={(event) => setEmail(event.target.value)}
                placeholder="example@email.com"
                required
              />
            </div>
            <div>
              <div>
              <label htmlFor="password">비밀번호</label>
              </div>
              <input
                id="password"
                type="password"
                value={password}
                onChange={(event) => setPassword(event.target.value)}
                placeholder="8자 이상 입력"
                minLength="8"
                required
              />
            </div>
            <div>
              <div>
              <label htmlFor="nickname">닉네임</label>
              </div>
              <input
                id="nickname"
                type="text"
                value={nickname}
                onChange={(event) => setNickname(event.target.value)}
                placeholder="닉네임 입력"
                required
              />
            </div>
        {message && <p style={{ color: "green" }}>{message}</p>}
        {error && <p style={{ color: "red" }}>{error}</p>}

        <button type="submit" disabled={isLoading} id="submitbutton">
          {isLoading ? "회원가입 중..." : "회원가입"}
        </button>
        <p id="sign_no_account">
            <span id="sign_no_account_text">이미 계정이 있나요?</span> <Link to="/login"><span id="go_signup">로그인</span></Link>
        </p>
        </div>
      </form>
    </main>
  );
}
