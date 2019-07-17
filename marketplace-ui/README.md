## Environment variables to config

**AWS_S3_BUCKET_DEPLOY**= The Bucket name to deploy later after making the build bundles.

**AWS_S3_REGION_DEPLOY**= The Bucket region of the Bucket already named in AWS_S3_BUCKET_DEPLOY.

**AWS_CLOUDFRONT_DISTRIBUTION_ID**= The CloudFront Distribution ID to update the objects after the Bucket already named in AWS_S3_BUCKET_DEPLOY is updated, which is related to the CloudFront Distribution ID.

**REACT_APP_DOMAIN_AUTH_CONFIG**= The tenant name from auth0.com(SAAS for AUTH2.0) to use in the authentication process.

**REACT_APP_CLIENT_ID_AUTH_CONFIG**=The Client ID from auth0.com which represents the client app to consume APIs.

**REACT_APP_AUDIENCE_ID_AUTH_CONFIG**=The Identifier or audience from auth0.com

**REACT_APP_CALL_BACK_URL_AUTH_CONFIG**= This is the URL that is used to get the JWT from auth0.com, please remember to include this on _Allowed Callback URLs_ in the application record on auth0.com. 

## Available Scripts

In the project directory, you can run with npm or yarn tool the :

### `npm start` or `yarn start`

Runs the app in the development mode.<br>
Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

The page will reload if you make edits.<br>
You will also see any lint errors in the console.

### `npm test` or `yarn test`

Launches the test runner in the interactive watch mode.<br>
See the section about [running tests](https://facebook.github.io/create-react-app/docs/running-tests) for more information.

### `npm run build` or `yarn build`

Builds the app for production to the `build` folder.<br>
It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.<br>
Your app is ready to be deployed!

See the section about [deployment](https://facebook.github.io/create-react-app/docs/deployment) for more information.

### `npm run deploy` or `yarn deploy`

Prepare the bundle and deploy to AWS S3 Bucket and CloudFront distribution update.

### `npm run eject` or `yarn build`

**Note: this is a one-way operation. Once you `eject`, you can’t go back!**

If you aren’t satisfied with the build tool and configuration choices, you can `eject` at any time. This command will remove the single build dependency from your project.

Instead, it will copy all the configuration files and the transitive dependencies (Webpack, Babel, ESLint, etc) right into your project so you have full control over them. All of the commands except `eject` will still work, but they will point to the copied scripts so you can tweak them. At this point you’re on your own.

You don’t have to ever use `eject`. The curated feature set is suitable for small and middle deployments, and you shouldn’t feel obligated to use this feature. However we understand that this tool wouldn’t be useful if you couldn’t customize it when you are ready for it.

## Prerequisites to Run or Build the App
- Have the AWS CLI installed with the user profile configuration for more info you can go on the [Configuring the AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-configure.html)
- Have the _Environment variables to config_ mentioned above with the correct values.
- Have the tool yarn or npm already installed.
## How to Run the App
Having the Prerequisites in place the only thing to do is just run one command:
- if you use prefer npm tool you can use `npm run start`
- if you use prefer yarn tool you can use `yarn start`
## How to make the Deployment
### Deploy to AWS S3 and CloudFront:
Having the Prerequisites in place the only thing to do is just run one command:
- if you use prefer npm tool you can use `npm run deploy`
- if you use prefer yarn tool you can use `yarn deploy`

