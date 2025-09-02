import { useState } from "react";
import { Menu, X, Briefcase } from "lucide-react";

const Navbar: React.FC = () => {
  const [isOpen, setIsOpen] = useState<boolean>(false);

  return (
    <nav className="w-full shadow-md bg-white fixed top-0 left-0 z-50">
      <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
        {/* Desktop Navbar */}
        <div className="hidden md:flex items-center justify-between h-16">
          {/* Left: Logo + Title */}
          <div className="flex items-center space-x-2">
            <Briefcase className="h-8 w-8 text-blue-600" />
            <span className="font-bold text-lg text-gray-800">CareerConnect</span>
          </div>

          {/* Center: Nav Links + Search + Login */}
         {/* Center: Nav Links + Search + Login */}
<div className="flex items-center space-x-8">
  <a href="#" className="text-gray-700 hover:text-blue-600">
    Jobs
  </a>
  <a href="#" className="text-gray-700 hover:text-blue-600">
    Companies
  </a>

  <input
    type="text"
    placeholder="Search..."
    className="px-3 py-1 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400"
  />

  <button className="bg-gradient-to-r from-blue-600 to-indigo-600 text-white px-5 py-2 rounded-full shadow-md hover:from-blue-700 hover:to-indigo-700 hover:scale-105 transition-transform duration-200">
    Login
  </button>
</div>

        </div>

        {/* Mobile Navbar */}
        <div className="flex md:hidden items-center justify-between h-16">
          <div className="flex items-center space-x-2">
            <Briefcase className="h-8 w-8 text-blue-600" />
            <span className="font-bold text-lg text-gray-800">CareerConnect</span>
          </div>

          <button
            onClick={() => setIsOpen(!isOpen)}
            className="p-2 rounded-md text-gray-700 hover:bg-gray-100"
            aria-label="Toggle Menu"
          >
            {isOpen ? <X className="h-6 w-6" /> : <Menu className="h-6 w-6" />}
          </button>
        </div>

        {/* Mobile Dropdown */}
        {isOpen && (
          <div className="md:hidden flex flex-col items-center space-y-4 py-4 bg-gray-50 border-t">
            <a href="#" className="text-gray-700 hover:text-blue-600">
              Jobs
            </a>
            <a href="#" className="text-gray-700 hover:text-blue-600">
              Companies
            </a>
            <input
              type="text"
              placeholder="Search..."
              className="px-3 py-1 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400 w-3/4"
            />
           <button className="bg-gradient-to-r from-blue-600 to-indigo-600 text-white px-5 py-2 rounded-full shadow-md hover:from-blue-700 hover:to-indigo-700 hover:scale-105 transition-transform duration-200">
  Login
</button>

          </div>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
