import { useState } from "react";
import { Link } from "react-router-dom";
import { login } from "../api/auth.js";
import "../css/Login.css"

export default function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (event) => {
    event.preventDefault();

    setMessage("");
    setError("");

    try {
      setIsLoading(true);

      const result = await login(email, password);

      localStorage.setItem("accessToken", result.data.accessToken);

      setMessage("로그인 성공!");
    } catch (err) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <main>
      <h1 id="main_text">로그인</h1>
      <form onSubmit={handleSubmit}>
        <div id="email_div">
            <label htmlFor="email">이메일</label>
              <input
                className="input"
                id="email"
                type="email"
                value={email}
                onChange={(event) => setEmail(event.target.value)}
                placeholder="example@email.com"
                required
              />
          </div>
        <div id="password_div">
          <label htmlFor="password">비밀번호</label>
          <input
            className="input"
            id="password"
            type="password"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
            placeholder="비밀번호 입력"
            required
          />
        </div>
        <p id="no_account">
            <span id="no_account_text">계정이 없나요?</span> <Link to="/signup"><span id="go_signup">회원가입</span></Link>
        </p>
        {message && <p style={{ color: "green" }} className="sys_msg">{message}</p>}
        {error && <p style={{ color: "red" }} className="sys_msg">{error}</p>}
        <button id="submit_button" type="submit" disabled={isLoading}>
          {isLoading ? "로그인 중..." : "로그인"}
        </button>
      </form>
    </main>
  );
}