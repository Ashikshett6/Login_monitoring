import React from "react";
import { Button, Table } from "react-bootstrap";

function BlockedUsersTable({ blockedUsers, onUnblock }) {
  return (
    <div className="table-responsive shadow-sm bg-white rounded p-3">
      <h5 className="mb-3">Blocked Users</h5>
      <Table bordered hover>
        <thead>
          <tr>
            <th>Username</th>
            <th>Failed Attempts</th>
            <th>Blocked At</th>
            <th>Reason</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {blockedUsers.length === 0 ? (
            <tr>
              <td colSpan="5" className="text-center">No blocked users</td>
            </tr>
          ) : (
            blockedUsers.map((user) => (
              <tr key={user.id}>
                <td>{user.username}</td>
                <td>{user.failedAttempts}</td>
                <td>{new Date(user.blockedAt).toLocaleString()}</td>
                <td>{user.reason}</td>
                <td>
                  <Button size="sm" variant="outline-primary" onClick={() => onUnblock(user.username)}>
                    Unblock
                  </Button>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </Table>
    </div>
  );
}

export default BlockedUsersTable;
