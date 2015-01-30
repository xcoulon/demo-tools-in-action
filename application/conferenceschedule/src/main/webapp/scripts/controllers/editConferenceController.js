

angular.module('conferenceschedule').controller('EditConferenceController', function($scope, $routeParams, $location, ConferenceResource , SessionResource) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.conference = new ConferenceResource(self.original);
            SessionResource.queryAll(function(items) {
                $scope.sessionsSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.name
                    };
                    if($scope.conference.sessions){
                        $.each($scope.conference.sessions, function(idx, element) {
                            if(item.id == element.id) {
                                $scope.sessionsSelection.push(labelObject);
                                $scope.conference.sessions.push(wrappedObject);
                            }
                        });
                        self.original.sessions = $scope.conference.sessions;
                    }
                    return labelObject;
                });
            });
        };
        var errorCallback = function() {
            $location.path("/Conferences");
        };
        ConferenceResource.get({ConferenceId:$routeParams.ConferenceId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.conference);
    };

    $scope.save = function() {
        var successCallback = function(){
            $scope.get();
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        };
        $scope.conference.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/Conferences");
    };

    $scope.remove = function() {
        var successCallback = function() {
            $location.path("/Conferences");
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        }; 
        $scope.conference.$remove(successCallback, errorCallback);
    };
    
    $scope.sessionsSelection = $scope.sessionsSelection || [];
    $scope.$watch("sessionsSelection", function(selection) {
        if (typeof selection != 'undefined' && $scope.conference) {
            $scope.conference.sessions = [];
            $.each(selection, function(idx,selectedItem) {
                var collectionItem = {};
                collectionItem.id = selectedItem.value;
                $scope.conference.sessions.push(collectionItem);
            });
        }
    });
    
    $scope.get();
});