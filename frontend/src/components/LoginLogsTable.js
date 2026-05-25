import React from "react";
import { Badge, Table } from "react-bootstrap";

function statusVariant(status) {
  if (status === "SUCCESS") return "success";
  if (status === "FAILED") return "danger";
  return "warning";
}

function LoginLogsTable({ logs }) {
  return (
    <div className="table-responsive shadow-sm bg-white rounded p-3 mb-4">
      <h5 className="mb-3">Recent Login Activities</h5>
      <Table hover bordered>
        <thead>
          <tr>
            <th>Username</th>
            <th>IP Address</th>
            <th>Login Time</th>
            <th>Browser</th>
            <th>Status</th>
            <th>Suspicious</th>
          </tr>
        </thead>
        <tbody>
          {logs.length === 0 ? (
            <tr>
              <td colSpan="6" className="text-center">No logs available</td>
            </tr>
          ) : (
            logs.map((log) => (
              <tr key={log.id}>
                <td>{log.username}</td>
                <td>{log.ipAddress}</td>
                <td>{new Date(log.loginTime).toLocaleString()}</td>
                <td className="truncate-cell">{log.browserDetails}</td>
                <td>
                  <Badge bg={statusVariant(log.loginStatus)}>{log.loginStatus}</Badge>
                </td>
                <td>
                  {log.suspicious ? (
                    <span className="text-danger fw-semibold">{log.suspiciousReason || "Yes"}</span>
                  ) : (
                    <span className="text-success">No</span>
                  )}
                </td>
              </tr>
            ))
          )}
        </tbody>
      </Table>
    </div>
  );
}

export default LoginLogsTable;
