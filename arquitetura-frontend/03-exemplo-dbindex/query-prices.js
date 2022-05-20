importScripts("https://unpkg.com/dexie@latest/dist/dexie.js");
importScripts("database-manager.js");

//
// Loop principal do web worker
//
self.addEventListener('message', async event => {
    const ticker = event.data.ticker;
    const from = event.data.from;
    const to = event.data.to;
    console.log("WW: querying prices for ticker " + ticker + ", from " + from + " to " + to + ".");

    var results = await DatabaseManager.query(ticker, from, to);
    console.log("WW: " + results.length + " results.");

    postMessage(results);
});