function (user, context, callback) {
    /** namespace cannot be an auth0 domain */
    const namespace = 'https://api.riggo.io/';
    context.idToken[namespace + 'siteId'] = '100';
    context.idToken[namespace + 'email'] = user.email;
    context.idToken[namespace + 'email_verified'] = user.email_verified;

    user.user_metadata = user.user_metadata || {};
    if(user.user_metadata.sf_account_id){
        context.idToken[namespace + 'sf_account_id'] = user.user_metadata.sf_account_id;
        context.accessToken[namespace + 'sf_account_id'] = user.user_metadata.sf_account_id;
    }
    if(user.user_metadata.sf_contact_id){
        context.idToken[namespace + 'sf_contact_id'] = user.user_metadata.sf_contact_id;           context.accessToken[namespace + 'sf_contact_id'] = user.user_metadata.sf_contact_id;
    }

    context.accessToken[namespace + 'siteId'] = '100';
    context.accessToken[namespace + 'email'] = user.email;
    context.accessToken[namespace + 'email_verified'] = user.email_verified;

    context.idToken[namespace + 'sfToken'] = context.clientMetadata;
    //context.idToken[namespace + 'sfToken2'] = context.clientMetadata.siteId;

    const assignedRoles = (context.authorization || {}).roles;

    let idTokenClaims = context.idToken || {};
    let accessTokenClaims = context.accessToken || {};

    idTokenClaims[`${namespace}roles`] = assignedRoles;
    accessTokenClaims[`${namespace}roles`] = assignedRoles;

    context.idToken = idTokenClaims;
    context.accessToken = accessTokenClaims;

    callback(null, user, context);
}