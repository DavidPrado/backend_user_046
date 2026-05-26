CREATE OR REPLACE VIEW v_accommodation_student_payment_details AS
select
	ap.id,
    p.name AS nome,
    p.cpf,
    COALESCE(
        (
            SELECT p2.name
            FROM responsible_student rs
            INNER JOIN person p2 ON p2.id = rs.id_person
            WHERE rs.id_student = s.id
            ORDER BY
                CASE
                    WHEN LOWER(rs.kinship) LIKE '%mãe%' OR LOWER(rs.kinship) LIKE '%mae%' THEN 1
                    WHEN LOWER(rs.kinship) LIKE '%pai%' THEN 2
                    WHEN LOWER(rs.kinship) LIKE '%avo%' OR LOWER(rs.kinship) LIKE '%avo%' THEN 3
                    ELSE 4
                END
            LIMIT 1
        ),
        'Sem responsável'
    ) AS responsavel,
    COALESCE(
        (
            SELECT p2.cpf
            FROM responsible_student rs
            INNER JOIN person p2 ON p2.id = rs.id_person
            WHERE rs.id_student = s.id
            ORDER BY
                CASE
                    WHEN LOWER(rs.kinship) LIKE '%mãe%' OR LOWER(rs.kinship) LIKE '%mae%' THEN 1
                    WHEN LOWER(rs.kinship) LIKE '%pai%' THEN 2
                    WHEN LOWER(rs.kinship) LIKE '%avó%' OR LOWER(rs.kinship) LIKE '%avo%' THEN 3
                    ELSE 4
                END
            LIMIT 1
        ),
        'N/A'
    ) AS cpf_responsavel,
    a.room_number AS quarto,
    a.building_block AS bloco,
    ap.reference_month AS mes_referencia,
    ap.payment_date AS data_pagamento,
    ap.status AS status_pagamento,
    ap.created_at
FROM person p
INNER JOIN student s ON s.id_person = p.id
LEFT JOIN accommodation_student ass ON ass.id_student = s.id
LEFT JOIN accommodation a ON a.id = ass.id_accommodation
LEFT JOIN accommodation_payment ap ON ap.id_student = s.id;
