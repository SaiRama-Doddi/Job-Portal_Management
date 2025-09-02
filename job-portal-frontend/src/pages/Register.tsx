// src/pages/Register.tsx
import { useState } from "react";
import { Loader2 } from "lucide-react";
import axios from "axios";

export default function Register() {
  const [step, setStep] = useState<"email" | "otp" | "role" | "details">("email");
  const [email, setEmail] = useState("");
  const [otp, setOtp] = useState("");
  const [loading, setLoading] = useState(false);
  const [verified, setVerified] = useState(false);
  const [role, setRole] = useState<"jobseeker" | "recruiter" | "">("");

  const handleSendOtp = async () => {
    setLoading(true);
    try {
      await axios.post("http://localhost:8081/api/auth/send-otp", { email });
      setStep("otp");
    } catch (e) {
      alert("Error sending OTP");
    }
    setLoading(false);
  };

  const handleVerifyOtp = async () => {
    setLoading(true);
    try {
      await axios.post("http://localhost:8081/api/auth/verify", { email, otp });
      setVerified(true);
      setStep("role");
    } catch {
      alert("Invalid OTP");
    }
    setLoading(false);
  };

  const handleRoleSelect = (selectedRole: "jobseeker" | "recruiter") => {
    setRole(selectedRole);
    setStep("details");
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-[#2596be] to-[#76b5c5] p-6">
      <div className="bg-white shadow-lg rounded-2xl w-full max-w-md p-8">
        <h1 className="text-2xl font-bold text-center text-[#063970] mb-6">
          Create your CareerConnect account
        </h1>

        {/* Step 1: Email */}
        {step === "email" && (
          <div className="space-y-4">
            <input
              type="email"
              placeholder="Enter your email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-[#2596be] outline-none"
            />
            <button
              onClick={handleSendOtp}
              disabled={loading || !email}
              className="w-full flex items-center justify-center bg-[#2596be] text-white py-2 rounded-lg hover:bg-[#063970] transition disabled:opacity-50"
            >
              {loading ? <Loader2 className="h-5 w-5 animate-spin" /> : "Send OTP"}
            </button>
          </div>
        )}

        {/* Step 2: OTP */}
        {step === "otp" && (
          <div className="space-y-4">
            <input
              type="text"
              placeholder="Enter OTP"
              value={otp}
              onChange={(e) => setOtp(e.target.value)}
              className="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-[#2596be] outline-none"
            />
            <button
              onClick={handleVerifyOtp}
              disabled={loading || !otp}
              className="w-full flex items-center justify-center bg-[#2596be] text-white py-2 rounded-lg hover:bg-[#063970] transition disabled:opacity-50"
            >
              {loading ? (
                <Loader2 className="h-5 w-5 animate-spin" />
              ) : verified ? (
                "✅ Verified"
              ) : (
                "Verify OTP"
              )}
            </button>
          </div>
        )}

        {/* Step 3: Role Selection */}
        {step === "role" && (
          <div className="space-y-4 text-center">
            <p className="text-gray-700 font-medium">Select your role:</p>
            <div className="flex gap-4">
              <button
                onClick={() => handleRoleSelect("jobseeker")}
                className="flex-1 bg-[#2596be] text-white py-2 rounded-lg hover:bg-[#063970]"
              >
                Job Seeker
              </button>
              <button
                onClick={() => handleRoleSelect("recruiter")}
                className="flex-1 bg-[#2596be] text-white py-2 rounded-lg hover:bg-[#063970]"
              >
                Recruiter
              </button>
            </div>
          </div>
        )}

        {/* Step 4: Details */}
        {step === "details" && role === "jobseeker" && (
          <form className="space-y-4">
            <input type="text" placeholder="Full Name" className="w-full px-4 py-2 border rounded-lg" />
            <input type="text" placeholder="Current Location" className="w-full px-4 py-2 border rounded-lg" />
            <input type="file" className="w-full px-4 py-2 border rounded-lg" />
            <button className="w-full bg-[#2596be] text-white py-2 rounded-lg hover:bg-[#063970]">
              Register
            </button>
          </form>
        )}

        {step === "details" && role === "recruiter" && (
          <form className="space-y-4">
            <input type="text" placeholder="Company Name" className="w-full px-4 py-2 border rounded-lg" />
            <input type="url" placeholder="Company Website" className="w-full px-4 py-2 border rounded-lg" />
            <input type="file" className="w-full px-4 py-2 border rounded-lg" />
            <button className="w-full bg-[#2596be] text-white py-2 rounded-lg hover:bg-[#063970]">
              Register
            </button>
          </form>
        )}
      </div>
    </div>
  );
}
