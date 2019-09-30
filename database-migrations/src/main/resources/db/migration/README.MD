
#Renamed files to match new Naming convention provided by Edward to prevent naming conflict 
#if multiple people end-up working on the same file 


#Standard provided 
Use a migration number between


201906181154 
or 
201907191210

as in 

YYYYMMDDhhmm

applies only to Rigo DB Migrations

## Clearing the database
delete from load_stop;
delete from location;
delete from address;
delete from load_line_item;
delete from load;
delete from shipper where id > 2;
delete from quote;
