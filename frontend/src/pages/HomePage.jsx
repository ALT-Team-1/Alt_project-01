import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import {
  createPost,
  deletePost,
  getPost,
  getPosts,
  updatePost,
} from "../api/posts";
import "../css/Home.css";

export default function HomePage() {
  const [posts, setPosts] = useState([]);
  const [selectedPost, setSelectedPost] = useState(null);

  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");

  const [editTitle, setEditTitle] = useState("");
  const [editContent, setEditContent] = useState("");

  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [isEditing, setIsEditing] = useState(false);

  const loadPosts = async () => {
    try {
      const data = await getPosts();
      setPosts(data);
    } catch (err) {
      setError(err.message);
    }
  };

  useEffect(() => {
    loadPosts();
  }, []);

  const handleCreate = async (event) => {
    event.preventDefault();

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




  return (
    <main className="home-page">
      <h1>Alt-과제 게시판</h1>
        <Link to="/signup">회원가입</Link>
        <Link to="/login">로그인</Link>
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