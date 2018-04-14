app.service('UserService', UserService);

function UserService($q, $http) {

	/*this.login = function(mail, password) {
		return $q(function(resolve, reject) {
			if (mail != 'errore@gmail.com') {
				resolve({
					userId : '0',
					mail : 'cerammerda@gioli.it',
					firstname : 'Luca',
					lastname : 'Ceragioli',
					username : 'Cerammerda',
					classe : "5",
					sezione : "F",
					password : 'vinciogay',
					roles : ['studente', 'moderatore', 'admin'],
					imageUrl : '',
					wallpaper : 'default',
					firstEntry: false //mettere su true per mostrare la pagina di first entry
				});
			} else {
				reject("Errore interno al server");
			}
		});
	} */
	
	this.login = function(mail,password) {
		var loginDTO = new Object();
		loginDTO.mail = mail;
		loginDTO.password = password;
		var req = {
				method: 'POST',
				url: 'http://localhost:8080/CopeApp/rest/login',
				headers: {
					'Content-Type': "text/JSON"
				},
				data: loginDTO
		}

		return $http(req);

	}

}