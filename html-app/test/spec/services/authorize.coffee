describe "Authorize service", ->
  authorize = null
  currentSession = null
  beforeEach module("gratefulplaceApp")
  beforeEach inject (Authorize, CurrentSession)->
    authorize = Authorize
    currentSession = CurrentSession

  it "authorizes content modification if the author id is the same as the current session id", ->
    content = {'author-id': 1}
    spyOn(currentSession).andReturn({id: 1})
    spec(authorize.canModifyContent(content)).toBeTruthy()
    