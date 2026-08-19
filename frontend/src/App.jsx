import { Routes, Route, Link, Form } from 'react-router-dom';
//라우터 부
import './css/App.css'
import Home from './pages/Home.jsx';

// 끝부분
function App() {
  return (
    <>
      <Routes>
        <Route path='/' element={<Home/>}/>
      </Routes>
    </>
  );
}
export default App;