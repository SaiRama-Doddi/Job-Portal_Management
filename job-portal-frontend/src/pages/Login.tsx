// src/pages/Login.tsx
import { useState } from "react";
import axios from "axios";
import { Loader2 } from "lucide-react";

export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);

  const handleLogin = async () => {
    setLoading(true);
    try {
      const res = await axios.post("http://localhost:8081/api/auth/login", { email, password });
      localStorage.setItem("token", res.data.token);

      // decode role from JWT if needed
      alert("Login successful ✅");
    } catch {
      alert("Invalid credentials ❌");
    }
    setLoading(false);
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-[#2596be] to-[#76b5c5] p-6">
      <div className="bg-white shadow-lg rounded-2xl w-full max-w-md p-8">
        <h1 className="text-2xl font-bold text-center text-[#063970] mb-6">Welcome Back</h1>

        <div className="space-y-4">
          <input
            type="email"
            placeholder="Email"
            className="w-full px-4 py-2 border rounded-lg"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <input
            type="password"
            placeholder="Password"
            className="w-full px-4 py-2 border rounded-lg"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />

          <button
            onClick={handleLogin}
            disabled={loading}
            className="w-full flex items-center justify-center bg-[#2596be] text-white py-2 rounded-lg hover:bg-[#063970] transition"
          >
            {loading ? <Loader2 className="h-5 w-5 animate-spin" /> : "Login"}
          </button>
        </div>
      </div>
    </div>
  );
}
