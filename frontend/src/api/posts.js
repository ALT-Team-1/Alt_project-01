const BASE_URL = "http://localhost:8080/api";

function getHeaders() {
  const accessToken = localStorage.getItem("accessToken");

  if (!accessToken) {
    throw new Error("로그인이 필요한 기능입니다.");
  }

  return {
    "Content-Type": "application/json",
    Authorization: `Bearer ${accessToken}`,
  };
}

async function request(url, options = {}) {
  const response = await fetch(`${BASE_URL}${url}`, options);
  const text = await response.text();
  const data = text ? JSON.parse(text) : null;

  if (!response.ok) {
    throw new Error(data?.message || "요청 처리에 실패했습니다.");
  }

  return data;
}

export function getPosts() {
  return request("/blogs");
}

export function getPost(id) {
  return request(`/blogs/${id}`);
}

export function createPost(title, content) {
  return request("/blogs", {
    method: "POST",
    headers: getHeaders(),
    body: JSON.stringify({ title, content }),
  });
}

export function updatePost(id, title, content) {
  return request(`/blogs/${id}`, {
    method: "PATCH",
    headers: getHeaders(),
    body: JSON.stringify({ title, content }),
  });
}

export function deletePost(id) {
  return request(`/blogs/${id}`, {
    method: "DELETE",
    headers: getHeaders(),
  });
}
