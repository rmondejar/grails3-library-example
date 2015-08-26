package auth

class AuthController {

    def springSecurityService
    def mailService

    //for testing
    def index() {
        render User.list()*.username
    }

    def signUp() {}

    def register() {

        //check user
        def newUser = User.findByUsername(params.username)
        if (newUser) {
            flash.message = message(code: "user.duplicated")
            render view:"SignUp"
            return
        }

        //check password confirmation
        if (params.password != params.password2) {
            flash.message =  message(code: "wrong.password.confirmation")
            render view:"SignUp"
            return
        }

        // Create user and role
        newUser = new User(username: params.username, password : params.password)
        newUser.save flush: true

        def userRole = Role.findByAuthority('ROLE_USER')

        UserRole.create(newUser, userRole, true)

        //redirect signIn
        flash.message = message(code: "user.register.successful", args:[newUser.username])
        redirect controller:"login", action: "auth", params: [ j_username: params.username ]

    }


    def lock() {
        def postUrl = request.contextPath + springSecurityService.securityConfig.apf.filterProcessesUrl
        [j_username: springSecurityService.currentUser, postUrl : postUrl]
    }

    def lost() {
        [username: params.username]
    }

    def reset() {

        //check existing user
        def user = User.findByUsername(params.username)
        if (!user) {
            flash.message = message(code: "user.not.found")
            render view:"lost", model:[username:params.username]
            return
        }

        //generate new password
        user.password = randomPass()

        //send new password by email
        try {
            println "Sending mail to ${params.username}"
            def success = mailService.sendMail {
                to params.username
                from 'youracount@gmail.com'
                subject "New password"
                text "Your new password is : ${user.password}"
            }

            if (success) {
                user.save flush: true

                //back to login
                flash.message = message(code: "lost.password.email.sent")
                flash.messagetype = "success"
                redirect action: "login"
                return
            } else
                flash.message = message(code: "lost.password.email.error")
        } catch(e) {
            flash.message = message(code: "lost.password.email.error")
            e.printStackTrace()
        }

        render view:"lost"

    }

    protected String randomPass() {
        UUID uuid = UUID.randomUUID()
        uuid.toString()[0..7]
    }

    def unauthorized() {}
}
