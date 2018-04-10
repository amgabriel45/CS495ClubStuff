var CFSRemark = function (container) {
    var dropdownsLoaded = [];
    var processOptions = function (list, options) {
        if ("sort" in options && options["sort"] == true) {
            list.sort(function (a, b) {
                return a.text < b.text ? -1 : (a.text > b.text ? 1 : 0);
            });
        }
        return list;
    }

    //Grant dropdown helpers
    var groups = [];
    container.refreshGroups = function (callback) {
        $.ajax({ method: "get", url: "api/groups" }).done(
                function (data) {
                    groups = data;
                    dropdownsLoaded.push("groups");
                    callback();
                });
    }
    container.getGroups = function (options) {
        options = options || {};
        var list = $.map(groups, function (obj) {
            obj.id = obj.Id.toString();
            obj.text = obj.Name;
            return obj;
        });
        return processOptions(list, options)
    }
    container.getGroupsWithEmpty = function (options) {
        return [{ "id": "", "text": " " }].concat(container.getGroups(options));
    }

    //Active Grant dropdown helpers
    var activeGrants = [];
    container.refreshActiveGrants = function (callback) {
        $.ajax({ method: "get", url: "/Shared/GetActiveGrantDropdown" }).done(
                function (data) {
                    activeGrants = data;
                    dropdownsLoaded.push("activeGrants");
                    callback();
                });
    }
    container.getActiveGrants = function (options) {
        options = options || {};
        var list = $.map(activeGrants, function (obj) {
            obj.id = obj.GrantId.toString();
            //Helen's change reverted...
            if (options.includeBanner)
                obj.text = obj.BannerAccount + " - " + obj.GrantNumber;
            else
                obj.text = obj.GrantNumber;
           // obj.text = obj.BannerAccount + " - " + obj.GrantNumber;
            return obj;
        });
        return processOptions(list, options)
    }
    container.getActiveGrantsWithEmpty = function (options) {
        return [{ "id": "", "text": " " }].concat(container.getActiveGrants(options));
    }

    //Project dropdown helpers
    var projects = [];
    container.refreshProjects = function (callback) {
        $.ajax({ method: "get", url: "/Shared/GetProjectDropdown" }).done(
                function (data) {
                    projects = data;
                    dropdownsLoaded.push("projects");
                    callback();
                });
    }
    container.getProjects = function (options) {
        options = options || {};
        var list = $.map(projects, function (obj) {
            obj.id = obj.ProjectId.toString();
            obj.text = obj.ProjectNumber;
            return obj;
        });
        return processOptions(list, options)
    }
    container.getProjectsWithEmpty = function (options) {
        return [{ "id": "", "text": " " }].concat(container.getProjects(options));
    }

    //Product dropdown helpers
    var products = [];
    container.refreshProducts = function (callback) {
        $.ajax({ method: "get", url: "/Shared/GetProductDropdown" }).done(
                function (data) {
                    products = data;
                    dropdownsLoaded.push("products");
                    callback();
                });
    }
    container.getProducts = function (options) {
        options = options || {};
        var list = $.map(products, function (obj) {
            obj.id = obj.ProductId.toString();
            obj.text = obj.ProductNumber;
            return obj;
        });
        return processOptions(list, options);
    }
    container.getProductsWithEmpty = function (options) {
        return [{ "id": "", "text": " " }].concat(container.getProducts(options));
    }

    //People dropdown helpers
    var people = [];
    container.refreshPeople = function (callback) {
        $.ajax({ method: "get", url: "/Shared/GetPeopleDropdown" }).done(
            function (data) {
                people = data;
                dropdownsLoaded.push("people");
                callback();
            });
    }
    container.getPeople = function (options) {
        options = options || {};
        if (!options.hasOwnProperty('includeResources')) options.includeResources = false;
        var list = $.map(people, function (obj) {
            obj.id = obj.Id.toString();
            obj.text = obj.PersonName;
            return obj;
        })
        // Don't include items that start with a space by default
        if (options.includeResources == false) {
            list = $.map(list, function (obj) {
                if (obj.text[0] == ' ') return null;
                else return obj;
            })
        }

        return processOptions(list, options);
    }
    container.getPeopleWithEmpty = function (options) {
        return [{ "id": "", "text": " " }].concat(container.getPeople(options));
    }


    //Teams dropdown helpers
    var teams = [];
    container.refreshTeams = function (callback) {
        $.ajax({ method: "get", url: "/Shared/GetTeamDropdown" }).done(
                function (data) {
                    teams = data;
                    dropdownsLoaded.push("teams");
                    callback();
                });
    }
    container.getTeams = function (options) {
        options = options || {};
        var list = $.map(teams, function (obj) {
            obj.id = obj.Id;
            obj.text = obj.TeamName;
            return obj;
        });
        return processOptions(list, options);
    }
    container.getTeamsWithEmpty = function (options) {
        return [{ "id": "", "text": " " }].concat(container.getTeams(options));
    }

    //Funding Sources dropdown helpers
    var fundingSources = [];
    container.refreshFundingSources = function (callback) {
        $.ajax({ method: "get", url: "/Shared/GetFundingSourceDropdown" }).done(
                function (data) {
                    fundingSources = data;
                    dropdownsLoaded.push("fundingsources");
                    callback();
                });
    }
    container.getFundingSources = function (options) {
        options = options || {};
        var list = $.map(fundingSources, function (obj) {
            obj.id = obj.Id;
            obj.text = obj.OrganizationName;
            return obj;
        });
        return processOptions(list, options);
    }
    container.getFundingSourcesWithEmpty = function (options) {
        return [{ "id": "", "text": " " }].concat(container.getFundingSources(options));
    }

    //Zoho dropdown helpers
    var zoho = [];
    container.refreshZoho = function (callback) {
        $.ajax({ method: "get", url: "/Shared/GetZohoDropdown" }).done(
                function (data) {
                    zoho = data;
                    dropdownsLoaded.push("zoho");
                    callback();
                });
    }
    container.getZoho = function (options) {
        options = options || {};
        var list = $.map(zoho, function (obj) {
            obj.id = obj.zohoIndex;
            obj.text = obj.zohoPotentialName;
            return obj;
        });
        return processOptions(list, options);
    }
    container.getZohoWithEmpty = function (options) {
        return [{ "id": "", "text": " " }].concat(container.getZoho(options));
    }

    //Callback manager for dropdown helpers
    container.requestDropdown = function (callback, dropdowns) {
        var hasAlreadyCalledback = false;
        var checkCallback = function () {
            for (var i = 0; i < dropdowns.length; i++) {
                if ($.inArray(dropdowns[i], dropdownsLoaded) == -1)
                    return;
            }
            if (!hasAlreadyCalledback) {
                hasAlreadyCalledback = true;
                callback();
                //Remove loading indicator
                $('#loadModal').modal('hide');
                $('#loadModal').remove();
            }
        }

        var loadingModal = $('\
        <div class="modal" id="loadModal"> \
            <div class="modal-dialog modal-center"> \
                <div class="modal-content"> \
                    <div class="modal-header"> \
                        <h4 class="modal-title center">Loading page</h4> \
                    </div> \
                    <div class="modal-body"> \
                        <div class="cfsloader"> </div> \
                    </div> \
                </div> \
            </div> \
        </div>');

        //Set loading indicator to appear in 0.5 seconds if the dropdowns aren't loaded
        loadingModal.insertAfter($('#topPage'));
        setTimeout(function () {
            if (!hasAlreadyCalledback)
                $('#loadModal').modal('show');
        }, 500);

        for (var i = 0; i < dropdowns.length; i++) {
            var method = findRefreshMethod(dropdowns[i]);
            if (method)
                method(checkCallback);
        }
    }

    var findRefreshMethod = function (method) {
        if (method === "groups")
            return container.refreshGroups;
        else if (method === "activeGrants")
            return container.refreshActiveGrants;
        else if (method === "projects")
            return container.refreshProjects;
        else if (method === "products")
            return container.refreshProducts;
        else if (method === "people")
            return container.refreshPeople;
        else if (method === "teams")
            return container.refreshTeams;
        else if (method === "fundingsources")
            return container.refreshFundingSources;
        else if (method === "zoho")
            return container.refreshZoho;
        else if (method === "holidays")
            return container.refreshHolidays;
        else if (method === "labordistributions")
            return container.refreshLaborDistribtuions;
        return null;
    }


    return container;
}(CFSRemark || {});