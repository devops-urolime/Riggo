/** adds dashboard menu to left hand menu.  */
INSERT INTO menu(
    name, type, site_id, parent_menu_id, url, rank, created_at)
VALUES ('Dashboard', 1, 100, 0, '', 1, now());