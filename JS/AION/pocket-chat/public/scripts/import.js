
function _importWallet(){
    var privateKey = $('#private-key').text();

    if (privateKey.length > 0) {
        var importedWallet = pocketAion.mastery.importWallet(privateKey);

        if (importedWallet instanceof Error) {
            $('#status').text("Failed to import wallet with error: "+importedWallet.message);
        }else {

        }
    }
}