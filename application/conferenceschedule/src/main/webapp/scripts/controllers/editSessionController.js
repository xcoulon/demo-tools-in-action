

angular.module('conferenceschedule').controller('EditSessionController', function($scope, $routeParams, $location, SessionResource , ConferenceResource, SpeakerResource) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.session = new SessionResource(self.original);
            ConferenceResource.queryAll(function(items) {
                $scope.conferenceSelectionList = $.map(items, function(item) {
                	var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                    	value : item.id,
                        text : item.name
                    };
                    if($scope.session.conference && item.id == $scope.session.conference.id) {
                        $scope.conferenceSelection = labelObject;
                        $scope.session.conference = wrappedObject;
                        self.original.conference = $scope.session.conference;
                    }
                    return labelObject;
                });
            });
            SpeakerResource.queryAll(function(items) {
                $scope.speakersSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.name
                    };
                    if($scope.session.speakers){
                        $.each($scope.session.speakers, function(idx, element) {
                            if(item.id == element.id) {
                                $scope.speakersSelection.push(labelObject);
                                $scope.session.speakers.push(wrappedObject);
                            }
                        });
                        self.original.speakers = $scope.session.speakers;
                    }
                    return labelObject;
                });
            });
        };
        var errorCallback = function() {
            $location.path("/Sessions");
        };
        SessionResource.get({SessionId:$routeParams.SessionId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.session);
    };

    $scope.save = function() {
        var successCallback = function(){
            $scope.get();
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        };
        $scope.session.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/Sessions");
    };

    $scope.remove = function() {
        var successCallback = function() {
            $location.path("/Sessions");
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        }; 
        $scope.session.$remove(successCallback, errorCallback);
    };
    
    $scope.$watch("conferenceSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.session.conference = {};
            $scope.session.conference.id = selection.value;
        }
    });
    $scope.speakersSelection = $scope.speakersSelection || [];
    $scope.$watch("speakersSelection", function(selection) {
        if (typeof selection != 'undefined' && $scope.session) {
            $scope.session.speakers = [];
            $.each(selection, function(idx,selectedItem) {
                var collectionItem = {};
                collectionItem.id = selectedItem.value;
                $scope.session.speakers.push(collectionItem);
            });
        }
    });
    
    $scope.get();
});