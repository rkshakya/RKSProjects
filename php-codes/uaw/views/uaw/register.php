<h2>Register</h2>
<?php echo validation_errors(); ?>

<?php echo form_open('login/register') ?>
<label for="email">Email</label> 
<input type="input" name="email" value="<?php echo set_value('email'); ?>" /><br />
<label for="password">Password</label>
<input type="password" name="password" /><br />
<label for="repassword">Retype Password</label>
<input type="password" name="repassword" /><br />		
<label for="fname">First Name</label> 
<input type="input" name="fname" value="<?php echo set_value('fname'); ?>" /><br />
<label for="lname">Last Name</label> 
<input type="input" name="lname" value="<?php echo set_value('lname'); ?>" /><br />
<label for="artist">Artist?</label> 
<select name="artist">
<option value="Cant hold a brush" <?php echo set_select('artist', 'Cant hold a brush', TRUE); ?> >Cant hold a brush</option>
<option value="Amateur" <?php echo set_select('artist', 'Amateur'); ?> >Amateur</option>
<option value="Professional" <?php echo set_select('artist', 'Professional'); ?> >Professional</option>
</select><br />

<label for="activist">Activist</label> 
<select name="activist">
<option value="Yes" <?php echo set_select('activist', 'Yes'); ?> >Yes</option>
<option value="No" <?php echo set_select('activist', 'No', TRUE); ?> >No</option>
<option value="Maybe" <?php echo set_select('activist', 'Maybe'); ?> >Maybe</option>
</select><br />
<label for="causeworking">Social cause(s) working on</label> 
<select name="causeworking[]" size=4 multiple>
<option name="child labour" value="child labour" <?php echo set_checkbox('causeworking[]', 'child labour'); ?> >child labour</option>
<option name="child abuse" value="child abuse" <?php echo set_checkbox('causeworking[]', 'child abuse'); ?> >child abuse</option>
<option name="suicide" value="suicide"  <?php echo set_checkbox('causeworking[]', 'suicide'); ?> >suicide</option> 
<option name="rape" value="rape" <?php echo set_checkbox('causeworking[]', 'rape'); ?> >rape</option>
</select><br />
<label for="causeinterest">Social cause(s) interested to know</label> 
<select name="causeinterest[]" size=4 multiple>
<option name="child labour" value="child labour" <?php echo set_checkbox('causeinterest[]', 'child labour'); ?> >child labour</option>
<option name="child abuse" value="child abuse" <?php echo set_checkbox('causeinterest[]', 'child abuse'); ?> >child abuse</option>
<option name="suicide" value="suicide" <?php echo set_checkbox('causeinterest[]', 'suicide'); ?> >suicide</option>
<option name="rape" value="rape" <?php echo set_checkbox('causeinterest[]', 'rape'); ?> >rape</option>
</select><br />
	<input type="submit" name="submit" value="Register" />
</form>

