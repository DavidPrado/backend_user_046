CREATE TABLE dashboard_stats (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    metric_name VARCHAR(50) NOT NULL,
    metric_value INT NOT NULL,
    category VARCHAR(50),
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_dashboard_category ON dashboard_stats(category);