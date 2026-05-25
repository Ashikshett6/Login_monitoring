import React from "react";
import { Navigate, Route, Routes } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import DashboardPage from "./pages/DashboardPage";
import NavbarComponent from "./components/NavbarComponent";

const isAuthenticated = () => Boolean(localStorage.getItem("token"));
const getRole = () => localStorage.getItem("role");

function ProtectedRoute({ children, adminOnly = false }) {
  if (!isAuthenticated()) {
    return <Navigate to="/login" replace />;
  }
  if (adminOnly && getRole() !== "ROLE_ADMIN") {
    return <Navigate to="/login" replace />;
  }
  return children;
}

function App() {
  return (
    <div>
      <NavbarComponent />
      <Routes>
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route
          path="/dashboard"
          element={
            <ProtectedRoute adminOnly>
              <DashboardPage />
            </ProtectedRoute>
          }
        />
      </Routes>
    </div>
  );
}

export default App;
