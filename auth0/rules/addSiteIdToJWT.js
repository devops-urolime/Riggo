function (user, context, callback) {
    /** namespace cannot be an auth0 domain */
    const namespace = 'https://auth.riggoqa.com/';
    context.idToken[namespace + 'siteId'] = '100';
    context.idToken[namespace + 'email'] = user.email;
    context.idToken[namespace + 'email_verified'] = user.email_verified;

    context.accessToken[namespace + 'siteId'] = '100';
    context.accessToken[namespace + 'email'] = user.email;
    context.accessToken[namespace + 'email_verified'] = user.email_verified;

    const assignedRoles = (context.authorization || {}).roles;

    let idTokenClaims = context.idToken || {};
    let accessTokenClaims = context.accessToken || {};

    idTokenClaims[`${namespace}/roles`] = assignedRoles;
    accessTokenClaims[`${namespace}/roles`] = assignedRoles;

    context.idToken = idTokenClaims;
    context.accessToken = accessTokenClaims;

    callback(null, user, context);
}