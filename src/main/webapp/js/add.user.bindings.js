function uiBinding() {
    getExampleXml();

    $('#addUser').click(function() {
        if ($('#newUser').validate()) {
            var newUser = new User();
            newUser.shortName = $('#shortName').val();
            newUser.foreName = $('#foreName').val();
            newUser.surName = $('#surName').val();
            newUser.email = $('#email').val();
            newUser.phoneNumber = $('#phoneNumber').val();
            newUser.submit();
        }
    });
}
