// Example object with ajax method
var CrimsonCalls = (function () {
    // ctor
    function self() { }

    // Ajax request method
    self.getMyClubs = function (params) {
        $.ajax({
            dataType:  "json",
            type:  'GET',
            contentType:  "application/json",
            data:  {},
            async:  true,
            processData:  true,
            url:  'api/clubs/',
            error: function (xhr, textStatus, errorThrown) {
                params.error(xhr, textStatus, errorThrown);
            },
            success: function (data, textStatus, xhr) {
                params.success(data);
            }
        });

    };


    // Return object
    return self;

})($);