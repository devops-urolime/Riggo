function (user, context, callback) {
    /** namespace cannot be an auth0 domain */
    const namespace = 'https://auth.riggoqa.com/';
    context.idToken[namespace + 'siteId'] = '100';
    context.idToken[namespace + 'email'] = user.email;
    context.idToken[namespace + 'email_verified'] = user.email_verified;

    context.accessToken[namespace + 'siteId'] = '100';
    context.accessToken[namespace + 'email'] = user.email;
    context.accessToken[namespace + 'email_verified'] = user.email_verified;

    callback(null, user, context);
}