importScripts("https://unpkg.com/dexie@latest/dist/dexie.js");

var DatabaseManager = (function() {
    var db = null;
    prepare();        

    // Prepares the database
    function prepare () {
        db = new Dexie("price_database");

        db.version(1).stores({
            assets: '++id,ticker,date,price'
        });

        db.open();
    }
  
    return {
        // Queries the database
        query: function (ticker, from, to) {
            return db.assets.
                where("ticker").anyOf(ticker).
                and(row => row.date >= from && row.date <= to).
                toArray();
        },
    
        // Updates the database
        save: function (ticker, data) {
            return db.transaction("rw", db.assets, function () {
                db.assets.where("ticker").anyOf(ticker).delete();
        
                data.forEach(e => {
                    db.assets.add({ ticker: ticker, date: e.date, price: e.price });
                });
        
            }).catch(function (e) {
                console.log("error: " + e);
            });
        }
    };
  })();

  
