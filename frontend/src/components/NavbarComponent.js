import React from "react";
import { Container, Nav, Navbar } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

function NavbarComponent() {
  const navigate = useNavigate();
  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");

  const handleLogout = () => {
    localStorage.clear();
    navigate("/login");
  };

  return (
    <Navbar bg="dark" variant="dark" expand="lg" className="mb-4">
      <Container>
        <Navbar.Brand style={{ cursor: "pointer" }} onClick={() => navigate("/login")}>
          Login Monitoring
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="navbar-nav" />
        <Navbar.Collapse id="navbar-nav">
          <Nav className="ms-auto">
            {!token && <Nav.Link onClick={() => navigate("/login")}>Login</Nav.Link>}
            {!token && <Nav.Link onClick={() => navigate("/register")}>Register</Nav.Link>}
            {token && role === "ROLE_ADMIN" && (
              <Nav.Link onClick={() => navigate("/dashboard")}>Dashboard</Nav.Link>
            )}
            {token && <Nav.Link onClick={handleLogout}>Logout</Nav.Link>}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}

export default NavbarComponent;
