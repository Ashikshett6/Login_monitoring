import React from "react";
import { Card, Col, Row } from "react-bootstrap";

function SummaryCards({ summary }) {
  const cards = [
    { title: "Successful Logins", value: summary.successCount ?? 0, color: "success" },
    { title: "Failed Logins", value: summary.failedCount ?? 0, color: "danger" },
    { title: "Blocked Accounts", value: summary.blockedCount ?? 0, color: "warning" }
  ];

  return (
    <Row className="g-3 mb-4">
      {cards.map((card) => (
        <Col md={4} key={card.title}>
          <Card className={`border-${card.color} shadow-sm`}>
            <Card.Body>
              <Card.Title>{card.title}</Card.Title>
              <h3 className={`text-${card.color}`}>{card.value}</h3>
            </Card.Body>
          </Card>
        </Col>
      ))}
    </Row>
  );
}

export default SummaryCards;
