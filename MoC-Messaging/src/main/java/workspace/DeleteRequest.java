/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workspace;

/**
 *
 * @author Luc
 */
public class DeleteRequest extends TeamRequest {

    public DeleteRequest(String competition, String teamname) {
        super(Action.DELETE, competition, teamname);
    }
}