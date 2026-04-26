-- =========================================
-- SEED DE DATOS - XploreNow
-- =========================================

-- Destinos
INSERT INTO destinos (nombre, pais) VALUES
    ('Buenos Aires', 'Argentina'),
    ('Bariloche', 'Argentina'),
    ('Mendoza', 'Argentina'),
    ('Cusco', 'Peru'),
    ('Rio de Janeiro', 'Brasil');

-- Categorias
INSERT INTO categorias (codigo, nombre) VALUES
    ('AVENTURA', 'Aventura'),
    ('CULTURA', 'Cultura'),
    ('GASTRONOMIA', 'Gastronomia'),
    ('NATURALEZA', 'Naturaleza'),
    ('RELAX', 'Relax'),
    ('FREE_TOUR', 'Free Tour'),
    ('VISITA_GUIADA', 'Visita Guiada'),
    ('EXCURSION', 'Excursion');

-- Actividades
INSERT INTO actividades (
    nombre, imagen_principal, destino_id, categoria_id,
    duracion_minutos, precio, cupos_disponibles,
    fecha_disponible_desde, fecha_disponible_hasta,
    descripcion, que_incluye, punto_encuentro,
    latitud, longitud,
    guia_asignado, idioma, politica_cancelacion
) VALUES
    ('Free Tour por San Telmo',
     'https://picsum.photos/seed/santelmo/600/400',
     1, 6, 120, 0.00, 25,
     '2026-01-01', '2026-12-31',
     'Recorrido a pie por el barrio mas antiguo de Buenos Aires. Plaza Dorrego, Mercado de San Telmo, calles empedradas.',
     'Guia profesional, recorrido completo. No incluye comidas.',
     'Plaza de Mayo, frente al Cabildo',
     -34.6083, -58.3712,
     'Martin Gonzalez', 'Espanol', 'Cancelacion gratuita hasta 24hs antes.'),

    ('Tour Gastronomico en Palermo',
     'https://picsum.photos/seed/palermo/600/400',
     1, 3, 180, 18500.00, 12,
     '2026-01-01', '2026-12-31',
     'Recorrido por 5 paradas gastronomicas: parrilla, empanadas, vinos argentinos, helado y dulce de leche.',
     'Todas las degustaciones, copa de vino, postres.',
     'Plaza Serrano, Palermo Soho',
     -34.5882, -58.4350,
     'Lucia Fernandez', 'Espanol/Ingles', 'Cancelacion 50% hasta 48hs antes.'),

    ('Trekking Cerro Catedral',
     'https://picsum.photos/seed/catedral/600/400',
     2, 1, 480, 32000.00, 8,
     '2026-03-01', '2026-11-30',
     'Trekking de dia completo al Cerro Catedral con vistas panoramicas al Nahuel Huapi.',
     'Equipo de seguridad, almuerzo, transporte ida y vuelta.',
     'Centro Civico de Bariloche',
     -41.1335, -71.3103,
     'Pedro Rios', 'Espanol', 'Cancelacion gratuita hasta 72hs antes por clima.'),

    ('Visita guiada Bodega Catena Zapata',
     'https://picsum.photos/seed/bodega/600/400',
     3, 7, 150, 22000.00, 20,
     '2026-01-01', '2026-12-31',
     'Recorrido por una de las bodegas mas premiadas del mundo. Cata incluida.',
     'Tour completo, cata de 4 vinos, traslado desde Mendoza centro.',
     'Hotel Park Hyatt, Mendoza',
     -32.8895, -68.8458,
     'Carla Esposito', 'Espanol/Ingles', 'Cancelacion 100% hasta 24hs antes.'),

    ('Excursion Machu Picchu desde Cusco',
     'https://picsum.photos/seed/machu/600/400',
     4, 8, 720, 95000.00, 30,
     '2026-01-01', '2026-12-31',
     'Excursion de dia completo a Machu Picchu en tren. Incluye guia bilingue.',
     'Tren ida y vuelta, entrada, almuerzo buffet, guia.',
     'Plaza de Armas, Cusco',
     -13.5163, -71.9786,
     'Carlos Quispe', 'Espanol/Ingles', 'Cancelacion 50% hasta 7 dias antes.'),

    ('Tour por Cristo Redentor',
     'https://picsum.photos/seed/cristo/600/400',
     5, 7, 240, 28000.00, 25,
     '2026-01-01', '2026-12-31',
     'Visita al Cristo Redentor en el cerro Corcovado. Vistas panoramicas de Rio.',
     'Tren del Corcovado, entrada, guia.',
     'Estacion Cosme Velho',
     -22.9519, -43.2105,
     'Beatriz Silva', 'Portugues/Espanol', 'Cancelacion 100% hasta 48hs antes.'),

    ('Clase de Tango en La Boca',
     'https://picsum.photos/seed/tango/600/400',
     1, 2, 90, 12000.00, 15,
     '2026-01-01', '2026-12-31',
     'Clase introductoria de tango con bailarines profesionales. No se requiere experiencia.',
     'Clase 90 min, copa de bienvenida.',
     'Caminito, La Boca',
     -34.6395, -58.3624,
     'Ana Rodriguez', 'Espanol/Ingles', 'Cancelacion gratuita hasta 24hs antes.'),

    ('Day Spa en Las Lenas',
     'https://picsum.photos/seed/spa/600/400',
     3, 5, 300, 45000.00, 6,
     '2026-06-01', '2026-09-30',
     'Dia completo de spa con masajes, sauna, piscina climatizada y vista a la montana.',
     'Acceso completo al spa, almuerzo, una sesion de masaje.',
     'Hotel Piscis, Las Lenas',
     -35.1481, -70.0830,
     'Sofia Mendoza', 'Espanol', 'Cancelacion 50% hasta 72hs antes.'),

    ('Avistaje de ballenas Puerto Madryn',
     'https://picsum.photos/seed/ballenas/600/400',
     1, 4, 240, 38000.00, 18,
     '2026-06-01', '2026-12-15',
     'Excursion en barco para avistar ballenas francas australes.',
     'Embarcacion, chaleco, guia naturalista, hidratacion.',
     'Puerto Piramides',
     -42.5750, -64.2778,
     'Lucas Fernandez', 'Espanol/Ingles', 'Cancelacion gratuita por clima.'),

    ('Free Tour Recoleta',
     'https://picsum.photos/seed/recoleta/600/400',
     1, 6, 90, 0.00, 30,
     '2026-01-01', '2026-12-31',
     'Recorrido por el cementerio de la Recoleta y los principales puntos del barrio.',
     'Guia profesional. A la gorra.',
     'Entrada del cementerio',
     -34.5875, -58.3927,
     'Diego Lopez', 'Espanol/Ingles', 'Cancelacion gratuita.'),

    ('Cabalgata en los Andes',
     'https://picsum.photos/seed/cabalgata/600/400',
     3, 1, 360, 35000.00, 10,
     '2026-09-01', '2026-04-30',
     'Cabalgata por la cordillera con guia gaucho. Asado al regreso.',
     'Caballo, equipamiento, asado completo, vino.',
     'Estancia La Alejandra',
     -33.0381, -68.8786,
     'Roberto Aguero', 'Espanol', 'Cancelacion 100% hasta 48hs antes.'),

    ('City tour panoramico Buenos Aires',
     'https://picsum.photos/seed/citytour/600/400',
     1, 7, 180, 15000.00, 40,
     '2026-01-01', '2026-12-31',
     'Recorrido por los barrios mas emblematicos: Plaza de Mayo, La Boca, San Telmo, Recoleta y Palermo.',
     'Bus turistico, guia bilingue.',
     'Obelisco, Av. 9 de Julio',
     -34.6037, -58.3816,
     'Mariana Torres', 'Espanol/Ingles/Portugues', 'Cancelacion gratuita hasta 24hs antes.');

-- Fotos de las galerias (algunas actividades, no todas)
INSERT INTO fotos_actividad (url, actividad_id) VALUES
    ('https://picsum.photos/seed/santelmo1/800/600', 1),
    ('https://picsum.photos/seed/santelmo2/800/600', 1),
    ('https://picsum.photos/seed/santelmo3/800/600', 1),
    ('https://picsum.photos/seed/palermo1/800/600', 2),
    ('https://picsum.photos/seed/palermo2/800/600', 2),
    ('https://picsum.photos/seed/catedral1/800/600', 3),
    ('https://picsum.photos/seed/catedral2/800/600', 3),
    ('https://picsum.photos/seed/bodega1/800/600', 4),
    ('https://picsum.photos/seed/bodega2/800/600', 4),
    ('https://picsum.photos/seed/machu1/800/600', 5),
    ('https://picsum.photos/seed/machu2/800/600', 5),
    ('https://picsum.photos/seed/machu3/800/600', 5);

-- =========================================
-- USUARIOS DE PRUEBA (Bloque A)
-- Password de todos: password123  (hasheada con BCrypt)
-- =========================================
INSERT INTO usuarios (email, password_hash, nombre, telefono, foto_url, creado_en) VALUES
    ('ana@xplorenow.com',   '$2a$10$VZsemoCC6/isNFPGfw4xbe2XWG2JTzt43vYSOLHr.ByA5DyDgHQkO', 'Ana Garcia',     '+541112345678', NULL, CURRENT_TIMESTAMP),
    ('juan@xplorenow.com',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Juan Perez',     '+541123456789', NULL, CURRENT_TIMESTAMP),
    ('maria@xplorenow.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Maria Rodriguez', '+541134567890', NULL, CURRENT_TIMESTAMP),
    ('pedro@xplorenow.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Pedro Lopez',    '+541145678901', NULL, CURRENT_TIMESTAMP),
    ('lucia@xplorenow.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Lucia Martinez', '+541156789012', NULL, CURRENT_TIMESTAMP);

-- Preferencias de viaje (ejemplos)
-- Ana: aventura + naturaleza
INSERT INTO usuario_preferencias (usuario_id, categoria_id) VALUES (1, 1), (1, 4);
-- Juan: gastronomia + cultura
INSERT INTO usuario_preferencias (usuario_id, categoria_id) VALUES (2, 3), (2, 2);
-- Maria: relax
INSERT INTO usuario_preferencias (usuario_id, categoria_id) VALUES (3, 5);

-- =========================================
-- HORARIOS DISPONIBLES (Bloque B)
-- Generamos varios horarios por actividad para los proximos meses
-- =========================================

-- Free Tour San Telmo (id=1): 3 horarios diarios para varios dias
INSERT INTO horarios_disponibles (actividad_id, fecha, hora, cupos_restantes) VALUES
    (1, '2026-05-15', '10:00', 25),
    (1, '2026-05-15', '15:00', 25),
    (1, '2026-05-16', '10:00', 25),
    (1, '2026-05-16', '15:00', 25),
    (1, '2026-06-01', '10:00', 25),
    (1, '2026-06-01', '15:00', 25);

-- Tour Gastronomico Palermo (id=2): 1 horario por dia
INSERT INTO horarios_disponibles (actividad_id, fecha, hora, cupos_restantes) VALUES
    (2, '2026-05-15', '19:00', 12),
    (2, '2026-05-20', '19:00', 12),
    (2, '2026-06-01', '19:00', 12);

-- Trekking Cerro Catedral (id=3): solo manana temprano
INSERT INTO horarios_disponibles (actividad_id, fecha, hora, cupos_restantes) VALUES
    (3, '2026-05-20', '08:00', 8),
    (3, '2026-05-25', '08:00', 8),
    (3, '2026-06-10', '08:00', 8);

-- Visita Bodega Catena (id=4): manana y tarde
INSERT INTO horarios_disponibles (actividad_id, fecha, hora, cupos_restantes) VALUES
    (4, '2026-05-15', '11:00', 20),
    (4, '2026-05-15', '15:00', 20),
    (4, '2026-05-22', '11:00', 20);

-- Excursion Machu Picchu (id=5): un solo horario, muy temprano
INSERT INTO horarios_disponibles (actividad_id, fecha, hora, cupos_restantes) VALUES
    (5, '2026-05-18', '05:00', 30),
    (5, '2026-06-05', '05:00', 30);

-- Tour Cristo Redentor (id=6)
INSERT INTO horarios_disponibles (actividad_id, fecha, hora, cupos_restantes) VALUES
    (6, '2026-05-15', '09:00', 25),
    (6, '2026-05-15', '14:00', 25);

-- Clase Tango La Boca (id=7)
INSERT INTO horarios_disponibles (actividad_id, fecha, hora, cupos_restantes) VALUES
    (7, '2026-05-15', '20:00', 15),
    (7, '2026-05-22', '20:00', 15);

-- =========================================
-- HORARIOS YA PASADOS (para probar FINALIZADA)
-- =========================================
INSERT INTO horarios_disponibles (actividad_id, fecha, hora, cupos_restantes) VALUES
    (1, '2026-04-10', '10:00', 23),
    (2, '2026-04-15', '19:00', 11);

-- =========================================
-- RESERVAS DE EJEMPLO (Bloque B)
-- Buscamos los horarios por fecha+hora para no depender de IDs autoasignados.
-- =========================================

-- Ana (id=1): reserva CONFIRMADA futura en Free Tour San Telmo (2026-05-15 10:00)
INSERT INTO reservas (usuario_id, actividad_id, horario_id, cantidad_participantes, estado, voucher_codigo, creada_en)
SELECT 1, 1, h.id, 2, 'CONFIRMADA', 'XPLR-A1B2C3D4', CURRENT_TIMESTAMP
FROM horarios_disponibles h
WHERE h.actividad_id = 1 AND h.fecha = '2026-05-15' AND h.hora = '10:00';

-- Juan (id=2): reserva CANCELADA en Tour Gastronomico (2026-05-20 19:00)
INSERT INTO reservas (usuario_id, actividad_id, horario_id, cantidad_participantes, estado, voucher_codigo, creada_en, cancelada_en)
SELECT 2, 2, h.id, 4, 'CANCELADA', 'XPLR-E5F6G7H8', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM horarios_disponibles h
WHERE h.actividad_id = 2 AND h.fecha = '2026-05-20' AND h.hora = '19:00';

-- Ana: reserva en horario PASADO -> el scheduler la marcara FINALIZADA
INSERT INTO reservas (usuario_id, actividad_id, horario_id, cantidad_participantes, estado, voucher_codigo, creada_en)
SELECT 1, 1, h.id, 2, 'CONFIRMADA', 'XPLR-PASADO01', CURRENT_TIMESTAMP
FROM horarios_disponibles h
WHERE h.actividad_id = 1 AND h.fecha = '2026-04-10' AND h.hora = '10:00';

-- Maria (id=3): tambien con horario pasado
INSERT INTO reservas (usuario_id, actividad_id, horario_id, cantidad_participantes, estado, voucher_codigo, creada_en)
SELECT 3, 2, h.id, 1, 'CONFIRMADA', 'XPLR-PASADO02', CURRENT_TIMESTAMP
FROM horarios_disponibles h
WHERE h.actividad_id = 2 AND h.fecha = '2026-04-15' AND h.hora = '19:00';
