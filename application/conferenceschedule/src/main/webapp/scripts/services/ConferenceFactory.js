angular.module('conferenceschedule').factory('ConferenceResource', function($resource){
    var resource = $resource('rest/conferences/:ConferenceId',{ConferenceId:'@id'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});