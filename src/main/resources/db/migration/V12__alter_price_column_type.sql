ALTER TABLE master_catalog.product
ALTER COLUMN price_per_unit TYPE double precision;

ALTER TABLE transaction.order
ALTER COLUMN total_price TYPE double precision;

ALTER TABLE transaction.order
ALTER COLUMN cash_paid_by_customer TYPE double precision;

ALTER TABLE transaction.order
ALTER COLUMN change TYPE double precision;

ALTER TABLE transaction.order_item 
ALTER COLUMN current_item_price TYPE double precision;
