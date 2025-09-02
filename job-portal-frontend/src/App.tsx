
import './App.css'

import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from './pages/Login';
import Home from './components/Home';
import Register from './pages/Register';

function App() {
 

  return (
    <>
   <Router>
    <Routes>
   
      <Route path="/" element={<Home />} />
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
    </Routes>
   </Router>
     
    </>
  )
}

export default App
