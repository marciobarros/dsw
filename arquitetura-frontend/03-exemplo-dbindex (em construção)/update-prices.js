//
// Loop principal do web worker
//
self.addEventListener('message', async event => {
    const ticker = event.data.ticker;
    console.log("WW: checking prices for ticker " + ticker + ".");

    const local_signature = event.data.signature;
    console.log("WW: the local signature is " + local_signature);

    const dataFilename = "https://marciobarros.github.io/unirio/bsi/dsw/price-files/" + ticker + "-monthly.csv";
    const signatureFilename = dataFilename + ".signature";

    const signature_response = await fetch(signatureFilename)
    const online_signature = await signature_response.text();
    console.log("WW: the online signature is " + online_signature);

    if (!local_signature || local_signature != online_signature) {
        console.log("WW: update required. Loading contents ...");
        const contents_response = await fetch(dataFilename);
        const contents = await contents_response.text();
        const results = convertContents(contents);

        console.log("WW: contents loaded.");
        postMessage({ signature: online_signature, contents: results });
    }
    else {
        console.log("WW: no update required.");
    }
});

//
// Converte o arquivo de preços para o formato JSON
//
function convertContents(contents) {
    var lines = contents.split('\n')
    var results = [];

    lines.slice(1).forEach(line => {
        var tokens = line.split(',');

        if (tokens.length == 2 && tokens[0].length > 0) {
            var date = tokens[0];
            var price = parseFloat(tokens[1]);
            results.push({ date: date, price: price });
        }
    });

    return results;
}