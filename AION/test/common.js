const Web3 = require('aion-web3');
const titanrc = require(`${process.cwd()}/titanrc.js`);
const networks = titanrc.blockchains.aion.networks
const {
    host,
    port,
    privateKey
} = networks.development
const provider = `${host}${port == undefined ? '' : ':' + port}`

const web3 = new Web3(new Web3.providers.HttpProvider(provider))

const compile = async function (w3, sol) {
    return new Promise((resolve, reject) => {
        w3.eth.compileSolidity(sol, (err, res) => {
            if (err) {
                //console.error(err)
                reject(err)
            }

            if (res) {
                //console.log(res)
                resolve(res)
            }
        })
    })
}

const setDefaultAccount = function (web3) {
    var account = web3.eth.accounts.privateKeyToAccount(privateKey);
    web3.eth.defaultAccount = account.address;
    web3.eth.accounts.wallet.add(account)
}

setDefaultAccount(web3);

module.exports = {
    compile,
    web3
}