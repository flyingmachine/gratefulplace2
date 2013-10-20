# Grateful Place

[Grateful Place](http://gratefulplace.com) is a wall-style forum which
uses Clojure for an API server, Datomic for the db, and Angular for
the front end.

## Development

### Getting it running - full stack

1. [Install leiningen](http://leiningen.org/#install)
2. Fork the repo
3. Clone the repo
4. `cd gp2/server`
5. `APP_ENV=memory lein run server` - this will start a web server on
   port 8080. Every time you start the server, the application will
   create an in-memory database and run all migrations. Two users are
   available by default: "flyingmachine" and "joebob", both with the
   password "password".

   *Optional* check out `server/resources/config/development.edn` for
   Datomic URI options. Run the app with `lein run server`
   (development is the default APP_ENV)
6. In a separte shell, `cd gp2/html-app`
7. `npm install; bower install`
8. `grunt server` - this will start the grunt server which will
   compile changes as you make them. Reload localhost:8080 to see your
   changes.

### Getting it running - just Angular app

1. Fork and clone the repo
2. `cd gp2/html-app`
3. `npm install; bower install`
4. `grunt server`

You might run into some weird issues when working with the Angular app
without the API server. This is because the $resource module gets
stubbed out, which you can see in
`gp2/html-app/app/scripts/init.coffee`. Personally, I think it's
better to get the API server running using the instructions. If
there's a better way to run the Angular app without the API server,
I'd love to see that implemented!

### Organization

* `gp2/server` contains the API server written in Clojure
* `gp2/html-app` contains a yeoman-generated Angular project
* Styles are in `gp2/html-app/app/styles`.
    * `_base.scss` defines common mixins and variables
    * `main.scss` defines basic styles and includes all other, more
      specialized styles
    * `_layout.scss` defines the grid
    * All other Sass partials (files beginning with an underscore) are
      more specialized in some way. `_posts.scss`, for example, has
      post-related styles.


### Pull requests

Please create a pull request for your changes :)

## Roadmap

* Drag and drop images
