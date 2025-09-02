// src/pages/Home.tsx
import { motion } from "framer-motion";

export default function Home() {
  return (
    <div className="h-screen flex">
      {/* Left Container */}
 <div className="w-1/2 flex items-center justify-center bg-gradient-to-b from-[#abdbe3] to-[#2596be] relative">

        <motion.div
          className="flex flex-col items-center justify-center"
          initial={{ opacity: 0, y: -40 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 1.5, ease: "easeOut" }}
        >
          {/* ✅ Ensure job-logo.svg is inside public/ folder */}
          <img
            src="/job-logo.png"
            alt="Career Connect Logo"
            className="w-20 h-20 mb-4 drop-shadow-lg"
          />
          <h1 className="text-4xl font-extrabold text-white tracking-wide drop-shadow-md">
            Career Connect
          </h1>
        </motion.div>
      </div>

      {/* Right Container */}
      <div className="w-1/2 flex items-center justify-center bg-gray-100">
        <motion.div className="text-center px-6" initial={{ opacity: 0, y: -40 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 1.5, ease: "easeOut" }}>
          <h2 className="text-4xl font-bold text-gray-800 mb-6">
            Great Things Are Coming 
          </h2>
          <p className="text-gray-600 mb-6">
            We’re working on something exciting! Stay tuned for the launch of
            Career Connect, your gateway to opportunities.
          </p>
          <button className="px-6 py-3 bg-[#2596be] text-white rounded-xl shadow-md hover:bg-blue-700 transition-all">
            Learn More
          </button>
        </motion.div>
      </div>
    </div>
  );
}
