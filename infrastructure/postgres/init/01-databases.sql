SELECT 'CREATE DATABASE ecommerce_user'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'ecommerce_user')\gexec
