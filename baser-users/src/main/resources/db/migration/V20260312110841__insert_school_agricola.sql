INSERT INTO school (
    code,
    name,
    email,
    phone,
    uf,
    city,
    neighborhood,
    street,
    number,
    complement,
    created_at,
    updated_at,
    created_by,
    updated_by
) VALUES (
    '046',   -- Código único (substitua se preferir outro formato)
    'Etec Prof. Carmelino Correa Junior',
     null,                      -- E-mail não informado
    '1637038035' ,                      -- Telefone não informado
    'SP',
    'Franca',
    'City Petrópolis',                      -- Bairro não informado
    'Rodovia Cândido Portinari',                      -- Logradouro não informado
    'km 405',                      -- Número não informado
    null,                      -- Complemento não informado
    NOW(),                     -- Data/hora atual de criação
    NOW(),                     -- Data/hora atual de atualização
    'system',                  -- Quem criou (ajuste conforme necessário)
    'system'                   -- Quem atualizou
);