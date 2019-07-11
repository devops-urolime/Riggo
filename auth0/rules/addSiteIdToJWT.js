/** In the future, this JS should:
 * Read X-API-Key from request header
 * Find X-API-Key in site_license.
 * Populate JWT Token with site.
  * @param user
 * @param context
 * @param callback
 */
function (user, context, callback) {
    //if (context.connection === 'company.com') {
    context.idToken['siteId'] = '100';
    //}

    callback(null, user, context);
}