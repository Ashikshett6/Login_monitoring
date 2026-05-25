import React, { useEffect, useState } from "react";
import { Alert, Container, Spinner } from "react-bootstrap";
import {
  getBlockedUsers,
  getDashboardSummary,
  getLoginLogs,
  unblockUser
} from "../services/api";
import SummaryCards from "../components/SummaryCards";
import LoginLogsTable from "../components/LoginLogsTable";
import BlockedUsersTable from "../components/BlockedUsersTable";

function DashboardPage() {
  const [summary, setSummary] = useState({});
  const [logs, setLogs] = useState([]);
  const [blockedUsers, setBlockedUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const loadDashboard = async () => {
    setError("");
    try {
      const [summaryRes, logsRes, blockedRes] = await Promise.all([
        getDashboardSummary(),
        getLoginLogs(),
        getBlockedUsers()
      ]);
      setSummary(summaryRes.data);
      setLogs(logsRes.data);
      setBlockedUsers(blockedRes.data);
    } catch (err) {
      if (err.response?.status === 401) {
        setError("Session expired. Redirecting to login...");
        return;
      }
      setError(err.response?.data?.error || "Failed to load dashboard data");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadDashboard();
    const interval = setInterval(loadDashboard, 15000);
    return () => clearInterval(interval);
  }, []);

  const onUnblock = async (username) => {
    try {
      await unblockUser(username);
      await loadDashboard();
    } catch (err) {
      setError(err.response?.data?.error || "Unable to unblock user");
    }
  };

  if (loading) {
    return (
      <Container className="text-center mt-5">
        <Spinner animation="border" />
      </Container>
    );
  }

  return (
    <Container className="pb-4">
      <h3 className="mb-3">Admin Login Monitoring Dashboard</h3>
      {error && <Alert variant="danger">{error}</Alert>}
      <SummaryCards summary={summary} />
      <LoginLogsTable logs={logs} />
      <BlockedUsersTable blockedUsers={blockedUsers} onUnblock={onUnblock} />
    </Container>
  );
}

export default DashboardPage;
