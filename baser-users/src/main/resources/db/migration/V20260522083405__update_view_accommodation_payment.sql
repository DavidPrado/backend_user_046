CREATE OR REPLACE VIEW v_accommodation_student_payment_details AS
SELECT
    ap.id,                         -- Agora garantido que nunca será nulo
    p.name AS nome,                -- Vindo da tabela person
    p.cpf,                         -- Vindo da tabela person
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
                    WHEN LOWER(rs.kinship) LIKE '%avó%' OR LOWER(rs.kinship) LIKE '%avo%' THEN 3
                    ELSE 4
                END
            LIMIT 1
        ),
        'Sem responsável'
    ) AS responsavel,              -- Atende ao campo responsavel
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
    ) AS cpf_responsavel,          -- Atende ao campo cpfResponsavel
    a.room_number AS quarto,       -- Da tabela accommodation
    a.building_block AS bloco,     -- Da tabela accommodation
    ap.reference_month AS mes_referencia, -- Da tabela accommodation_payment
    ap.payment_date AS data_pagamento,   -- Da tabela accommodation_payment
    ap.status AS status_pagamento,       -- Da tabela accommodation_payment
    ap.created_at                  -- Usado para ordenações e auditoria
FROM accommodation_payment ap     -- INVERTIDO: Começamos pela tabela que MANDADO existir registros
INNER JOIN student s ON s.id = ap.id_student
INNER JOIN person p ON p.id = s.id_person
LEFT JOIN accommodation_student ass ON ass.id_student = s.id
LEFT JOIN accommodation a ON a.id = ass.id_accommodation;