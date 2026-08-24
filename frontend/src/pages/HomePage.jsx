import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import {
  createPost,
  deletePost,
  getPost,
  getPosts,
  updatePost,
} from "../api/posts";
import "../css/Home.css";

export default function HomePage() {
  const navigate = useNavigate();
  const [isLoggedIn, setIsLoggedIn] = useState(
    Boolean(localStorage.getItem("accessToken"))
  );
  const nickname = localStorage.getItem("nickname");
  const [posts, setPosts] = useState([]);
  const [selectedPost, setSelectedPost] = useState(null);
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [editTitle, setEditTitle] = useState("");
  const [editContent, setEditContent] = useState("");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [isEditing, setIsEditing] = useState(false);

  //게시물 목록 들고 오기
  //불러오는거여서 event 필요X
  const loadPosts = async () => {
    try {
      const data =
         await getPosts();
      setPosts(data);
    } catch (err) {
      setError(err.message);
    }
  };
  useEffect(() => {
    loadPosts();
  }, []);

  //작성
  const handleCreate = async (event) => {
    event.preventDefault();
    if (!isLoggedIn) {
      alert("로그인이 필요한 기능입니다.");
      navigate("/login");
      return;
    }
    setMessage("");
    setError("");
    try {
      await createPost(title, content);
      setTitle("");
      setContent("");
      setMessage("게시글이 작성되었습니다.");
      loadPosts();
    } catch (err) {
      setError(err.message);
    }
  };

  //제목 누르면 상세글 나오게 하기
  const handlePostClick = async (id) => {
    setMessage("");
    setError("");
    setIsEditing(false);
    try {
      const data = await getPost(id);
      setSelectedPost(data);
      setEditTitle(data.title);
      setEditContent(data.content);
    } catch (err) {
      setError(err.message);
    }
  };

  //수정
  const handleUpdate = async (event) => {
    event.preventDefault();
    try {
      const updatedPost = await updatePost(
        selectedPost.id,
        editTitle,
        editContent
      );
      setSelectedPost(updatedPost);
      setIsEditing(false);
      setMessage("게시글이 수정되었습니다.");
      loadPosts();
    } catch (err) {
      setError(err.message);
    }
  };
  //삭제
  const handleDelete = async () => {
    const isConfirmed = window.confirm("정말 게시글을 삭제할까요?");
    if (!isConfirmed) return;
    try {
      await deletePost(selectedPost.id);
      setSelectedPost(null);
      setMessage("게시글이 삭제되었습니다.");
      loadPosts();
    } catch (err) {
      setError(err.message);
    }
  };

  //로그아웃
  const handleLogout = () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("userId");
    localStorage.removeItem("nickname");
    setIsLoggedIn(false);
    setSelectedPost(null);
    setIsEditing(false);
    setTitle("");
    setContent("");
    setError("");
    setMessage("로그아웃되었습니다.");
  };

  return (
    <main className="home-page">
      <div className="head">
        <h1 id="Home_main_text">Alt-과제 게시판</h1>
        <div id="link">
          {isLoggedIn ? (
            <div className="user-menu">
              <span className="user-name">{nickname || "사용자"}님</span>
              <button type="button" className="logout-button" onClick={handleLogout}>
                로그아웃
              </button>
            </div>
          ) : (
            <>
              <div className="Home_button"><Link to="/signup">회원가입</Link></div>
              <div className="Home_button"><Link to="/login">로그인</Link></div>
            </>
          )}
        </div>
      </div>
      {isLoggedIn ? (
        <section className="post-create">
          <h2>게시글 작성</h2>

          <form onSubmit={handleCreate}>
            <input
              type="text"
              placeholder="제목을 입력하세요"
              value={title}
              onChange={(event) => setTitle(event.target.value)}
              required
            />

            <textarea
              placeholder="내용을 입력하세요"
              value={content}
              onChange={(event) => setContent(event.target.value)}
              required
            />

            <button type="submit">게시글 작성</button>
          </form>
        </section>
      ) : (
        <section className="post-create">
          <h2>게시글 작성</h2>
          <button
            type="button"
            onClick={() => {
              alert("로그인이 필요한 기능입니다.");
              navigate("/login");
            }}
          >
            게시글 작성
          </button>
        </section>
      )}

      {message && <p className="success-message">{message}</p>}
      {error && <p className="error-message">{error}</p>}

      <section className="post-list">
        <h2>게시글 목록</h2>

        {posts.map((post) => (
          <button
            className="post-item"
            key={post.id}
            onClick={() => handlePostClick(post.id)}
          >
            <strong>{post.title}</strong>
            <span>작성자: {post.author}</span>
          </button>
        ))}
      </section>

      {selectedPost && (
        <section className="post-detail">
          {isEditing ? (
            <form onSubmit={handleUpdate}>
              <input
                value={editTitle}
                onChange={(event) => setEditTitle(event.target.value)}
                required
              />

              <textarea
                value={editContent}
                onChange={(event) => setEditContent(event.target.value)}
                required
              />

              <button type="submit">수정 완료</button>
              <button type="button" onClick={() => setIsEditing(false)}>
                취소
              </button>
            </form>
          ) : (
            <>
              <h2>{selectedPost.title}</h2>
              <p>{selectedPost.content}</p>
              <span>작성자: {selectedPost.author}</span>

              <div className="post-buttons">
                <button onClick={() => setIsEditing(true)}>수정</button>
                <button onClick={handleDelete}>삭제</button>
              </div>
            </>
          )}
        </section>
      )}
    </main>
  );
}
